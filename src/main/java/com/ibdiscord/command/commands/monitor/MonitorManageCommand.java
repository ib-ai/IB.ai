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

package com.ibdiscord.command.commands.monitor;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.entries.GuildData;
import com.ibdiscord.utils.UString;

import java.util.List;
import java.util.Set;

public abstract class MonitorManageCommand extends Command {

    /**
     * Creates the command.
     */
    MonitorManageCommand(String name) {
        super(name,
                CommandPermission.role(GuildData.MODERATOR),
                Set.of()
        );
    }

    /**
     * Checks if the input is valid.
     * @param context The command context.
     * @param input The input.
     * @return True if it is, false otherwise.
     */
    protected abstract boolean isValidInput(CommandContext context, String input);

    /**
     * Adds the input.
     * @param context The command context.
     * @param input The input.
     */
    protected abstract void add(CommandContext context, String input);

    /**
     * Removes the input.
     * @param context The command context.
     * @param input The input.
     */
    protected abstract void remove(CommandContext context, String input);

    /**
     * Lists all entries.
     * @param context The command context.
     * @return A list of entries.
     */
    protected abstract List<String> list(CommandContext context);

    /**
     * Takes the input, validates it, then adds or removes it.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        if(context.getArguments().length < 1) {
            context.reply(__(context, "error.missing_operation"));
            return;
        }
        if(context.getArguments().length < 2) {
            context.reply(__(context, "error.missing_data"));
            return;
        }
        String input = UString.concat(context.getArguments(), " ", 1);
        if(!isValidInput(context, input)) {
            context.reply(__(context, "error.invalid_data"));
            return;
        }
        switch(context.getArguments()[0].toLowerCase()) {
            case "create":
            case "add":
                add(context, input);
                break;
            case "delete":
            case "remove":
                remove(context, input);
                break;
            default:
                context.reply(__(context, "error.invalid_operation"));
                return;
        }
        context.reply(__(context, "success.done"));
    }
}
