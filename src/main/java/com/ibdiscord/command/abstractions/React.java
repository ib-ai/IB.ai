/* Copyright 2017-2020 Arraying
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

package com.ibdiscord.command.abstractions;

import com.ibdiscord.command.CommandAction;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.react.ReactionData;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class React implements CommandAction {

    /**
     * Modifies the data.
     * @param data The data.
     * @param emote The emote.
     * @param roleIDs The role.
     */
    protected abstract void modifyData(ReactionData data, String emote, List<String> roleIDs);

    /**
     * Modifies the message.
     * @param message The message.
     * @param emote The emote.
     */
    protected abstract void modifyMessage(Message message, Object emote);

    /**
     * Handles the command.
     * @param context The command context.
     */
    @Override
    public final void accept(CommandContext context) {
        context.assertArguments(1, "error.missing_channelid");
        context.assertArguments(2, "error.missing_messageid");
        context.assertArguments(3, "error.missing_emojiid");
        context.assertArguments(4, "error.missing_roleid");

        long channelId;
        long messageId;
        ArrayList<String> roleArgs;
        try {
            channelId = Long.valueOf(context.getArguments()[0]);
            messageId = Long.valueOf(context.getArguments()[1]);
            roleArgs = new ArrayList<>(Arrays.asList(context.getArguments()).subList(3, context.getArguments().length));
        } catch(NumberFormatException exception) {
            context.replyI18n("error.reaction_invalidid");
            return;
        }
        TextChannel channel = context.getGuild().getTextChannelById(channelId);
        if(channel == null) {
            context.replyI18n("error.reaction_channel");
            return;
        }
        Message message;
        try {
            message = channel.retrieveMessageById(messageId).complete();
        } catch(IllegalArgumentException exception) {
            context.replyI18n("error.reaction_message");
            return;
        }

        // Checking that all roleIDs are valid
        ArrayList<String> roleIDData = new ArrayList<>(roleArgs);
        ArrayList<Role> roles = new ArrayList<>();
        roleArgs.stream()
                .map(arg -> arg.replace("!", ""))
                .forEach(id -> roles.add(context.getGuild().getRoleById(id)));
        if(roles.isEmpty() || roles.contains(null)) {
            context.replyI18n("error.reaction_role");
            return;
        }
        ReactionData data = DataContainer.INSTANCE.getGravity().load(
                new ReactionData(context.getGuild().getId(), messageId)
        );

        String emoteRaw = context.getArguments()[2];
        modifyData(data, emoteRaw, roleIDData);
        modifyMessage(message,
                context.getMessage().getEmotes().isEmpty() ? emoteRaw : context.getMessage().getEmotes().get(0));
        DataContainer.INSTANCE.getGravity().save(data);
        context.replyI18n("success.done");
    }

}
