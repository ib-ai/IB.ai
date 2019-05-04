/**
 * Copyright 2017-2019 Arraying
 *
 * This file is part of LoyalBot.
 *
 * LoyalBot is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LoyalBot is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LoyalBot. If not, see http://www.gnu.org/licenses/.
 */

package dev.vardy.command.commands;

import dev.vardy.command.Command;
import dev.vardy.command.CommandContext;
import dev.vardy.command.permissions.CommandPermission;
import dev.vardy.utils.UInput;
import net.dv8tion.jda.core.entities.Role;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public final class UserRolesCommand extends Command {

    /**
     * Executes the command.
     */
    public UserRolesCommand() {
        super("userroles",
                Set.of("myroles", "showroles"),
                CommandPermission.discord(),
                new HashSet<>()
        );
    }

    /**
     * Shows a list of user roles.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        var target = context.getMember();
        if(context.getArguments().length >= 1) {
            target = UInput.getMember(context.getGuild(), context.getArguments()[0]);
        }
        if(target == null) {
            context.reply("User not found!");
            return;
        }
        context.reply("This user has the following roles: `%s`.", target.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.joining(", "))
        );
    }

}
