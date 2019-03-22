package com.ibdiscord.command.commands.reminder;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.pagination.Pagination;
import com.ibdiscord.reminder.Reminder;
import com.ibdiscord.reminder.ReminderHandler;
import net.dv8tion.jda.core.EmbedBuilder;

import java.text.SimpleDateFormat;
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
public final class ReminderListCommand extends Command {

    /**
     * Creates the command.
     */
    ReminderListCommand() {
        super("list",
                Set.of("l"),
                CommandPermission.discord(),
                new HashSet<>()
        );
    }

    /**
     * Lists all reminders.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        int page = 1;
        if(context.getArguments().length > 0) {
            try {
                page = Integer.valueOf(context.getArguments()[0]);
            } catch(IllegalArgumentException ignored) {}
        }
        List<Reminder> reminders = ReminderHandler.INSTANCE.getFor(context.getMember().getUser()).stream()
                .filter(it -> !it.isCompleted())
                .collect(Collectors.toList());
        if(reminders.isEmpty()) {
            context.reply("You have no active reminders.");
            return;
        }
        Pagination<Reminder> pagination = new Pagination<>(reminders, 10);
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setDescription("Here is a list of your active reminders.")
                .setFooter("Page " + page + "/" + pagination.total(), null);
        pagination.page(page).forEach(it -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM d, yyyy, HH:mm:ss z");
            String title = String.format("%s (ID: %d)",
                    simpleDateFormat.format(it.getValue().getDate()),
                    it.getValue().getId()
            );
            embedBuilder.addField(title, it.getValue().getReminder(), false);
        });
        context.reply(embedBuilder.build());
    }

}
