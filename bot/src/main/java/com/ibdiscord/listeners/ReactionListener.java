package com.ibdiscord.listeners;

import com.ibdiscord.data.db.DContainer;
import com.ibdiscord.data.db.entries.ReactionData;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageReaction;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * Copyright 2019 Arraying
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
        ReactionData reactionData = DContainer.INSTANCE.getGravity().load(new ReactionData(guild.getId(), message));
        String roleId = reactionData.get(emote).asString();
        if(roleId == null) {
            return;
        }
        Role role = guild.getRoleById(roleId);
        if(role == null) {
            return;
        }
        if(add) {
            guild.getController().addSingleRoleToMember(member, role).queue(null, Throwable::printStackTrace);
        } else {
            guild.getController().removeSingleRoleFromMember(member, role).queue(null, Throwable::printStackTrace);
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
