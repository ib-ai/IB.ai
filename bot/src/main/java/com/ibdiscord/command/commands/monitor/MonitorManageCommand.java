package com.ibdiscord.command.commands.monitor;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.entries.GuildData;
import com.ibdiscord.utils.UString;

import java.util.List;
import java.util.Set;

/**
 * Copyright 2019 Arraying
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
