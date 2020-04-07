/* Copyright 2018-2020 Arraying, Jarred Vardy <vardy@riseup.net>
 *
 * This file is part of IB.ai.
 *
 * IB.ai is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * IB.ai is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with IB.ai. If not, see http://www.gnu.org/licenses/.
 */

package com.ibdiscord.listeners;

import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.cassowary.CassowariesData;
import com.ibdiscord.data.db.entries.cassowary.CassowaryData;
import com.ibdiscord.data.db.entries.react.EmoteData;
import com.ibdiscord.data.db.entries.react.ReactionData;
import com.ibdiscord.vote.VoteCache;
import com.ibdiscord.vote.VoteEntry;
import de.arraying.gravity.data.property.Property;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.*;
import java.util.stream.Collectors;

public final class ReactionListener extends ListenerAdapter {

    /**
     * Triggered when a reaction is added from a message in a private chat,
     * private group DM or within a guild.
     * Uses a case to filter the event down to a single group type.
     * @param event The event.
     */
    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if(event.getMember() == null) {
            return;
        }
        react(event.getMember(), event.getMessageIdLong(), getEmoji(event.getReactionEmote()), true);
        if(event.getMember().getUser().getIdLong() == event.getJDA().getSelfUser().getIdLong()) {
            return;
        }
        switch(event.getReactionEmote().getName()) {
            case "\uD83D\uDC4D": // thumbs up
                react(event.getMessageIdLong(), (short) 0);
                break;
            case "\uD83D\uDC4E": // thumbs down
                react(event.getMessageIdLong(), (short) 2);
                break;
            case "\u2B50": // star
                event.getChannel().retrieveMessageById(event.getMessageIdLong()).queue(message -> {
                    if(message.getAuthor().getIdLong() == event.getMember().getUser().getIdLong()
                            && message.getReactions().stream()
                            // here it checks whether the :star: reaction has no reactions beforehand
                            // this is so that the message only gets triggered with initial stars, not looking at anyone
                            // in particular who always does this
                            .anyMatch(react -> react.getReactionEmote().getName().equals("\u2B50")
                                    && react.getCount() <= 1)) {
                        roast(message);
                    }
                });
                break;
            default:
                break;
        }
    }

    /**
     * Triggered when a reaction is removed from a message in a private chat,
     * private group DM or within a guild.
     * Uses a case to filter the event down to a single group type.
     * @param event The event.
     */
    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
        if(event.getMember() == null) {
            return;
        }
        react(event.getMember(), event.getMessageIdLong(), getEmoji(event.getReactionEmote()), false);
        if(event.getMember().getUser().getIdLong() == event.getJDA().getSelfUser().getIdLong()) {
            return;
        }
        switch(event.getReactionEmote().getName()) {
            case "\uD83D\uDC4D": // thumbs up
                react(event.getMessageIdLong(), (short) 1);
                break;
            case "\uD83D\uDC4E": // thumbs down
                react(event.getMessageIdLong(), (short) 3);
                break;
            default:
                break;
        }
    }

    /**
     * Handles the reaction and adding/removing roles.
     * @param member The member.
     * @param message The message ID.
     * @param emote The emoji ID.
     * @param add True to add, false to remove.
     */
    private void react(Member member, long message, String emote, boolean add) {
        Guild guild = member.getGuild();
        ReactionData reactionData = DataContainer.INSTANCE.getGravity().load(new ReactionData(guild.getId(), message));
        EmoteData emoteData = DataContainer.INSTANCE.getGravity().load(new EmoteData(
                reactionData.get(emote).asString())
        );

        Collection<Role> positiveRoles = emoteData.contents().stream()
                .filter(prop -> !prop.asString().startsWith("!"))
                .map(prop -> member.getGuild().getRoleById(prop.defaulting(0L).asLong()))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Collection<Role> negativeRoles = emoteData.contents().stream()
                .filter(prop -> prop.asString().startsWith("!"))
                .map(prop -> prop.asString().replace("!", ""))
                .map(prop -> member.getGuild().getRoleById(prop))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // Tried to incorporate the 'add' bool into the collection of
        // the positive and negative roles instead of doing this conditional but
        // got some weird behaviour. Perhaps make less verbose in future.
        Collection<Role> rolesToAdd = add ? positiveRoles : negativeRoles;
        Collection<Role> rolesToRemove = add ? negativeRoles : positiveRoles;

        // Check to stop Pre-IB students from getting the NSFW role.
        // This is basically a HARDXXX CASSOWARY
        try {
            Role nsfwRole = guild.getRolesByName("NSFW", true).get(0);
            Role preIBRole = guild.getRolesByName("Pre-IB", true).get(0);

            List<Role> userRoles = member.getRoles();
            if(rolesToAdd.contains(nsfwRole) && userRoles.stream()
                    .anyMatch(role -> role.getName().equals("Pre-IB"))) {
                return;
            }
            if(rolesToAdd.contains(preIBRole) && userRoles.stream()
                    .anyMatch(role -> role.getName().equals("NSFW"))) {
                rolesToRemove.add(nsfwRole);
            }
        } catch(Exception ex) {
            // ignored
        }

        CassowariesData cassowariesData = DataContainer.INSTANCE.getGravity().load(new CassowariesData(guild.getId()));
        for(Property cassowariesProp : cassowariesData.values()) {

            CassowaryData cassowaryData = DataContainer.INSTANCE.getGravity().load(new CassowaryData(
                    guild.getId(),
                    cassowariesProp.asString())
            );
            boolean containsRoleToAdd = !Collections.disjoint(cassowaryData.values().stream()
                    .map(Property::asString)
                            .collect(Collectors.toSet()),
                    rolesToAdd.stream()
                            .map(ISnowflake::getId)
                            .collect(Collectors.toSet()));

            // if a role that is about to be added to the user is a member of the cassowary
            if(containsRoleToAdd) {
                // for each role ID inside the cassowary,
                for(Property cassowaryProp : cassowaryData.values()) {
                    // for each role the user has
                    for(Role userRole : member.getRoles()) {
                        if(cassowaryProp.asString().equals(userRole.getId())) {
                            // add user's role to rolesToRemove
                            rolesToRemove.add(userRole);
                        }
                    }
                }
            }
        }

        List<Role> roles = new ArrayList<>(member.getRoles());
        roles.removeAll(rolesToRemove);
        roles.addAll(rolesToAdd);
        guild.modifyMemberRoles(member, roles).queue();
    }

    /**
     * Handles the reaction for votes.
     * @param message The message ID.
     * @param action The action.
     */
    private void react(long message, short action) {
        VoteEntry entry = VoteCache.INSTANCE.get(message);
        if(entry != null) {
            switch(action) {
                case 0:
                    entry.voteYes();
                    break;
                case 1:
                    entry.unvoteYes();
                    break;
                case 2:
                    entry.voteNo();
                    break;
                case 3:
                    entry.unvoteNo();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Roasts the creator of the message.
     * @param message The message object.
     */
    private void roast(Message message) {
        String tag = message.getAuthor().getAsMention();
        message.getChannel().sendMessage(tag + " did you really just star your own message? \uD83E\uDD26").queue();
    }

    /**
     * Gets the emoji of the reaction.
     * @param emote The emote object.
     * @return An emoji.
     */
    private String getEmoji(MessageReaction.ReactionEmote emote) {
        return emote.isEmote()
                ? emote.getEmote().getAsMention()
                : emote.getName();
    }

}
