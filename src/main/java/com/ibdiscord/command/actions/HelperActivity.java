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
import com.ibdiscord.utils.UInput;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.PermissionOverride;
import net.dv8tion.jda.api.entities.Role;

import java.util.List;
import java.util.stream.Collectors;

public class HelperActivity implements CommandAction {

    /**
     * Returns number of helper messages.
     * @param context The command context.
     */
    @Override
    public void accept(CommandContext context) {
        context.assertArguments(1, "error.generic_syntax_arg");

        Member member = UInput.getMember(context.getGuild(), context.getArguments()[0]);

        if (member == null) {
            context.replyI18n("error.helper_invalid");
            return;
        }

        List<String> roles = member.getRoles().stream()
            .map(Role::getName)
            .filter(roleName -> roleName.toLowerCase().endsWith("helper"))
            .collect(Collectors.toList());

        roles.forEach(role -> {
            List<GuildChannel> subjectChannels = context.getGuild().getChannels().stream()
                    .filter(guildChannel -> {
                        PermissionOverride perm = guildChannel.getRolePermissionOverrides().stream()
                                .filter(permissionOverride -> permissionOverride.getRole().getName().equals(role))
                                .findFirst().orElse(null);
                        return perm != null;
                    })
                    .collect(Collectors.toList());
        });
    }
}
