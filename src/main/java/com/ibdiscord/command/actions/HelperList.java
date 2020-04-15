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
import com.ibdiscord.utils.UInput;
import net.dv8tion.jda.api.entities.*;

public final class HelperList implements CommandAction {

    /**
     * Creates list of helpers.
     * @param context The command context.
     */
    @Override
    public void accept(CommandContext context) {

        context.assertArguments(1, "error.generic_syntax_arg");

        String desiredRole = context.getArguments()[0];
        String helperRoleInChannel;

        Role role = UInput.getRole(context.getGuild(), desiredRole);

        if (role != null) {
            helperRoleInChannel = role.getId();
        } else {
            if (context.getMessage().getMentionedChannels().size() > 0) {
                TextChannel mentionedChannel = context.getMessage().getMentionedChannels().get(0);

                helperRoleInChannel = checkHelperRole(mentionedChannel);
            } else {
                desiredRole = desiredRole + " Helper";
                Role roleFinal = context.getGuild().getRolesByName(desiredRole, true).stream()
                        .findFirst().orElse(null);
                if (roleFinal == null) {
                    context.replyI18n("error.helper_list_incorrect");
                    return;
                }
                helperRoleInChannel = roleFinal.getId();
            }
        }

        if (helperRoleInChannel == null) {
            context.replyI18n("error.helper_list_channel");
            return;
        }

        Role roleFinal = context.getGuild().getRoleById(helperRoleInChannel);
        context.replyEmbed(UEmbed.helperMessageEmbed(context.getGuild(), roleFinal));
    }

    private String checkHelperRole(GuildChannel mentionedChannel) {
        PermissionOverride finalReturnRole = mentionedChannel.getRolePermissionOverrides()
                .stream().filter(permissionOverride ->
                        permissionOverride.getRole()
                                .getName()
                                .toLowerCase()
                                .endsWith("helper")).findFirst().orElse(null);

        return finalReturnRole == null ? null : finalReturnRole.getId();
    }
}
