package com.ibdiscord.command.commands.reminder;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.reminder.Reminder;
import com.ibdiscord.reminder.ReminderHandler;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
