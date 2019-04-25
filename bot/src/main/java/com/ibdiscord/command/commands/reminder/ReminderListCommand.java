package com.ibdiscord.command.commands.reminder;

import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.commands.abstracted.PaginatedCommand;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.pagination.Page;
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
public final class ReminderListCommand extends PaginatedCommand<Reminder> {

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
     * Gets the pagination of the reminders.
     * @param context The command context.
     * @return The pagination.
     */
    @Override
    protected Pagination<Reminder> getPagination(CommandContext context) {
        List<Reminder> reminders = ReminderHandler.INSTANCE.getFor(context.getMember().getUser()).stream()
                .filter(it -> !it.isCompleted())
                .collect(Collectors.toList());
        return new Pagination<>(reminders, 10);
    }

    /**
     * Formats the reminder into the embed.
     * @param context The context.
     * @param embedBuilder The embed builder.
     * @param page The page.
     */
    @Override
    protected void handle(CommandContext context, EmbedBuilder embedBuilder, Page<Reminder> page) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM d, yyyy, HH:mm:ss z");
        String title = String.format("%s (ID: %d)",
                simpleDateFormat.format(page.getValue().getDate()),
                page.getValue().getId()
        );
        embedBuilder.addField(title, page.getValue().getReminder(), false);
    }

    /**
     * Adds a description.
     * @param context The context.
     * @param embedBuilder The embed builder.
     */
    @Override
    protected void tweak(CommandContext context, EmbedBuilder embedBuilder) {
        embedBuilder.setDescription("Here is a list of your active reminders.");
    }

}
