/**
 * Copyright 2017-2019 Arraying, Jarred Vardy <jarred.vardy@gmail.com>
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
import com.ibdiscord.data.db.entries.react.EmoteData;
import com.ibdiscord.data.db.entries.react.ReactionData;
import com.ibdiscord.vote.VoteCache;
import com.ibdiscord.vote.VoteEntry;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageReaction;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.Collection;
import java.util.Objects;
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
        EmoteData emoteData = DataContainer.INSTANCE.getGravity().load(new EmoteData(reactionData.get(emote).asString()));

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
        Collection<Role> rolesToAdd = add ? positiveRoles: negativeRoles;
        Collection<Role> rolesToRemove = add ? negativeRoles: positiveRoles;

        // Check to stop Pre-IB students from getting the NSFW role.
        Role preIBRole = guild.getRolesByName("Pre-IB", true).get(0);
        Role nsfwRole = guild.getRolesByName("NSFW", true).get(0);
        if(rolesToAdd.contains(nsfwRole) && member.getRoles().contains(preIBRole)) {
            return;
        }

        // Second function in sequence used in consuming lambda in order to ensure first function has finished
        // without blocking the thread.
        guild.getController().removeRolesFromMember(member, rolesToRemove).queue(success ->
                    guild.getController().addRolesToMember(member, rolesToAdd).queue(null, Throwable::printStackTrace),
                    Throwable::printStackTrace);
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
            }
        }
    }

    /**
     * Gets the emoji of the reaction.
     * @param emote The emote object.
     * @return An emoji.
     */
    private String getEmoji(MessageReaction.ReactionEmote emote) {
        return emote.isEmote() ?
                emote.getEmote().getAsMention() :
                emote.getName();
    }

}
