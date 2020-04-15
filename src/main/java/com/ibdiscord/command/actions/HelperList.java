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
        Role roleFinal = UInput.getRole(context.getGuild(), desiredRole);

        if (roleFinal == null) {
            if (context.getMessage().getMentionedChannels().size() > 0) {
                TextChannel mentionedChannel = context.getMessage().getMentionedChannels().get(0);

                PermissionOverride rolePermissionOverride = mentionedChannel.getRolePermissionOverrides()
                        .stream().filter(permissionOverride ->
                                permissionOverride.getRole()
                                        .getName()
                                        .toLowerCase()
                                        .endsWith("helper")).findFirst().orElse(null);

                if (rolePermissionOverride == null) {
                    context.replyI18n("error.helper_list_channel");
                    return;
                }

                roleFinal = rolePermissionOverride.getRole();
            } else {
                desiredRole = desiredRole + " Helper";
                roleFinal = UInput.getRole(context.getGuild(), desiredRole);

                if (roleFinal == null) {
                    context.replyI18n("error.helper_list_incorrect");
                    return;
                }
            }
        }

        context.replyEmbed(UEmbed.helperMessageEmbed(context.getGuild(), roleFinal));
    }
}
