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

package dev.vardy.command.commands.monitor;

import dev.vardy.command.Command;
import dev.vardy.command.CommandContext;
import dev.vardy.command.permissions.CommandPermission;
import dev.vardy.data.db.entries.GuildData;
import dev.vardy.utils.UString;

import java.util.List;
import java.util.Set;

public abstract class MonitorManageCommand extends Command {

    /**
     * Creates the command.
     */
    MonitorManageCommand(String name) {
        super(name,
                Set.of(),
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
            context.reply("Please provide the operation.");
            return;
        }
        if(context.getArguments().length < 2) {
            context.reply("Please provide the data.");
            return;
        }
        String input = UString.concat(context.getArguments(), " ", 1);
        if(!isValidInput(context, input)) {
            context.reply("The data provided is invalid (e.g. not a proper user).");
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
                context.reply("Invalid operation.");
                return;
        }
        context.reply("Consider it done.");
    }

}
