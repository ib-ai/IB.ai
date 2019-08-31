package com.ibdiscord.command.commands;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.localisation.Localiser;
import com.ibdiscord.utils.UInput;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Copyright 2017-2019 Arraying
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
public final class UserRolesCommand extends Command {

    /**
     * Executes the command.
     */
    public UserRolesCommand() {
        super("userroles",
                Set.of("myroles", "showroles"),
                CommandPermission.discord(),
                Set.of()
        );
    }

    /**
     * Shows a list of user roles.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        Member target = context.getMember();
        if(context.getArguments().length >= 1) {
            target = UInput.getMember(context.getGuild(), context.getArguments()[0]);
        }
        if(target == null) {
            context.reply(Localiser.__(context, "error.user_404"));
            return;
        }
        context.reply(Localiser.__(context, "info.user_roles", target.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.joining(", ")))
        );
    }

}
