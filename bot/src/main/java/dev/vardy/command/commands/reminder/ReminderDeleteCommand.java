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
import dev.vardy.reminder.Reminder;
import dev.vardy.reminder.ReminderHandler;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class ReminderDeleteCommand extends Command {

    /**
     * Creates the command.
     */
    ReminderDeleteCommand() {
        super("delete",
                Set.of("d", "remove"),
                CommandPermission.discord(),
                new HashSet<>()
        );
    }

    /**
     * Executes the command.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        if(context.getArguments().length == 0) {
            context.reply("Please specify the reminder ID. It is shown when listing all reminders.");
            return;
        }
        List<Reminder> reminders = ReminderHandler.INSTANCE.getFor(context.getMember().getUser()).stream()
                .filter(it -> !it.isCompleted())
                .collect(Collectors.toList());
        String id = context.getArguments()[0];
        Reminder reminder = reminders.stream()
                .filter(it -> !it.isCompleted())
                .filter(it -> String.valueOf(it.getId()).equals(id))
                .findFirst()
                .orElse(null);
        if(reminder == null) {
            context.reply("You have no (active) reminders with that ID.");
            return;
        }
        reminder.setCompleted(true);
        context.reply("The reminder has been deleted.");
    }

}
