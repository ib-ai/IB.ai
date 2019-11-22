/* Copyright 2017-2019 Jarred Vardy
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

package com.ibdiscord.command.commands;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.entries.GuildData;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.managers.GuildManager;
import net.dv8tion.jda.api.managers.RoleManager;

import java.util.Objects;
import java.util.Set;

public class PingableCommand extends Command {

    /**
     * Creates a PingableCommand
     */
    public PingableCommand() {
        super("pingable",
                CommandPermission.role(GuildData.MODERATOR),
                Set.of()
        );
        this.correctUsage = "pingable <role ID>";
    }

    /**
     * Toggles the pingability of a role.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        if(context.getArguments().length != 1) {
            sendUsage(context);
        }

        Long roleId = null;
        try {
            roleId = Long.valueOf(context.getArguments()[0]);
        } catch (NumberFormatException exception) {
            sendUsage(context);
        }

        if(roleId != null && context.getGuild().getRoleById(roleId) != null) {
            boolean isMentionable = Objects.requireNonNull(context.getGuild().getRoleById(roleId)).isMentionable();
            RoleManager roleManager = Objects.requireNonNull(context.getGuild().getRoleById(roleId)).getManager();
            roleManager.setMentionable(!isMentionable).queue();
        } else {
            sendUsage(context);
        }
    }
}
