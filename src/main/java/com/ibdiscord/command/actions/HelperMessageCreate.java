/* Copyright 2020 Ray Clark
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

package com.ibdiscord.command.actions;

import com.ibdiscord.command.CommandAction;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.helper.HelperMessageData;
import com.ibdiscord.data.db.entries.helper.HelperMessageRolesData;
import com.ibdiscord.utils.UEmbed;
import com.ibdiscord.utils.UInput;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.concurrent.RejectedExecutionException;

public final class HelperMessageCreate implements CommandAction {

    /**
     * Creates the helper message.
     * @param context The command context.
     */
    @Override
    public void accept(CommandContext context) {
        context.assertArguments(2, "error.generic_arg_length");
        if (context.getMessage().getMentionedChannels().size() < 1) {
            context.replyI18n("error.missing_channel");
            return;
        }
        TextChannel channel = context.getMessage().getMentionedChannels().get(0);
        Role role = UInput.getRole(context.getGuild(), context.getArguments()[0]);
        if (role == null) {
            context.replyI18n("error.missing_roleid");
            return;
        }

        HelperMessageRolesData helperMessageRolesData = DataContainer.INSTANCE.getGravity().load(
                new HelperMessageRolesData(context.getGuild().getId())
        );

        HelperMessageData helperMessageData = DataContainer.INSTANCE.getGravity().load(
                new HelperMessageData(context.getGuild().getId(), role.getId())
        );

        if (helperMessageRolesData.contains(role.getId())) {
            if (helperMessageData.getKeys().contains(channel.getId())) {
                channel.deleteMessageById(helperMessageData.get(channel.getId()).asString()).queue();
            }
        }

        try {
            Message message = channel.sendMessage(UEmbed.helperMessageEmbed(context.getGuild(), role))
                    .complete();
            helperMessageRolesData.add(role.getId());
            helperMessageData.set(channel.getId(), message.getId());
            DataContainer.INSTANCE.getGravity().save(helperMessageRolesData);
            DataContainer.INSTANCE.getGravity().save(helperMessageData);
            message.pin().queue();
            context.replyI18n("success.done");
        } catch (RejectedExecutionException e) {
            context.replyI18n("error.pin_channel");
        }
    }
}
