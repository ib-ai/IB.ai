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

package dev.vardy.command.commands.reminder;

import dev.vardy.command.Command;
import dev.vardy.command.CommandContext;
import dev.vardy.command.permissions.CommandPermission;
import dev.vardy.reminder.ReminderHandler;
import dev.vardy.utils.UString;
import dev.vardy.utils.UTime;

import java.util.HashSet;
import java.util.Set;

public final class ReminderCreateCommand extends Command {

    /**
     * Creates the command.
     */
    ReminderCreateCommand() {
        super("create",
                Set.of("c", "add", "new"),
                CommandPermission.discord(),
                new HashSet<>()
        );
    }

    /**
     * Creates a reminder.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        if(context.getArguments().length < 1) {
            context.reply("Please provide the duration until you want to be reminded.");
            return;
        }
        if(context.getArguments().length < 2) {
            context.reply("Please provide the reminder.");
            return;
        }
        long duration = UTime.parseDuration(context.getArguments()[0]);
        if(duration == -1) {
            context.reply("Your duration is invalid. For example, a reminder in 3 hours and 15 minutes would be `3h15`.");
            return;
        }
        String reminder = UString.concat(context.getArguments(), " ", 1);
        ReminderHandler.INSTANCE.create(context.getMember().getUser(), duration, reminder);
        context.reply("The reminder has been scheduled!");
    }

}
