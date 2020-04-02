/* Copyright 2017-2020 Arraying
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

package com.ibdiscord.command.abstractions;

import com.ibdiscord.command.CommandAction;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.utils.UString;

import java.util.List;

public abstract class MonitorManage implements CommandAction {

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
    public void accept(CommandContext context) {
        context.assertArguments(1, "error.missing_operation");
        context.assertArguments(2, "error.missing_data");

        String input = UString.concat(context.getArguments(), " ", 1);
        if(!isValidInput(context, input)) {
            context.replyI18n("error.invalid_data");
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
                context.replyI18n("error.invalid_operator");
                return;
        }
        context.replyI18n("success.done");
    }

}
