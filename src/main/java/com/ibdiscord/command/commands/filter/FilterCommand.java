/* Copyright 2018-2020 Arraying
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

package com.ibdiscord.command.commands.filter;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import net.dv8tion.jda.api.Permission;

import java.util.Set;

public final class FilterCommand extends Command {

    /**
     * Creates the command.
     */
    public FilterCommand() {
        super("filter",
                CommandPermission.discord(Permission.MANAGE_SERVER),
                Set.of(new FilterCreateCommand(),
                        new FilterDeleteCommand(),
                        new FilterListCommand(),
                        new FilterToggleCommand()
                )
        );
        this.correctUsage = "filter <create/delete/list> [message/page]";
    }

    /**
     * Sends the usage message.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        sendUsage(context);
    }

}
