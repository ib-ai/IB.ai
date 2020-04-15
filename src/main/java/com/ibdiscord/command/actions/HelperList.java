/* Copyright 2020 Nathaneal Varghese
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
import com.ibdiscord.utils.UEmbed;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public final class HelperList implements CommandAction {

    /**
     * Creates list of helpers.
     * @param context The command context.
     */
    @Override
    public void accept(CommandContext context) {

        MessageChannel channelMessage = context.getChannel();
        String desiredRole;

        List<Role> allRoles = context.getGuild().getRoles();

        if (context.getArguments().length < 1) {

            String helperIdInChannel = checkHelperRole(allRoles, (GuildChannel) channelMessage);

            if (helperIdInChannel == null) {
                context.replyI18n("error.helper_list_channel");
                return;
            }

            Role roleFinal = context.getGuild().getRoleById(helperIdInChannel);
            context.replyEmbed(UEmbed.helperMessageEmbed(context.getGuild(), roleFinal));
            return;
        }

        if (context.getArguments().length == 1) {
            desiredRole = context.getArguments()[0];

            AtomicReference<String> helperRoleInChannel = new AtomicReference<>();

            if (desiredRole.matches("\\d*")) {
                try {
                    helperRoleInChannel.set(desiredRole);
                } catch (Exception e) {
                    context.replyI18n("error.generic");
                }

            } else {
                try {
                    TextChannel mentionedChannel = context.getMessage().getMentionedChannels().get(0);

                    String helperIdInChannel = checkHelperRole(allRoles, mentionedChannel);

                    helperRoleInChannel.set(helperIdInChannel);

                } catch (IndexOutOfBoundsException e) {
                    desiredRole = desiredRole + " Helper";
                    context.getGuild().getRolesByName(desiredRole, true).forEach(role -> {
                        helperRoleInChannel.set(role.getId());
                    });

                }
            }

            try {
                Role roleFinal = context.getGuild().getRoleById(helperRoleInChannel.get());
                context.replyEmbed(UEmbed.helperMessageEmbed(context.getGuild(), roleFinal));
            } catch (IllegalArgumentException e) {
                context.replyI18n("error.helper_list_incorrect");
            }
        }
    }

    private String checkHelperRole(List<Role> allRoles, GuildChannel mentionedChannel) {
        Role finalReturnRole = allRoles.stream().filter(role -> {
            if (role.hasPermission(mentionedChannel, Permission.MESSAGE_MANAGE)) {
                return role.getName().toLowerCase().endsWith("helper");
            }
            return false;
        }).findFirst().orElse(null);

        return finalReturnRole == null ? null : finalReturnRole.getId();
    }
}
