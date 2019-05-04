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

import dev.vardy.command.CommandContext;
import dev.vardy.command.commands.abstracted.PaginatedCommand;
import dev.vardy.command.permissions.CommandPermission;
import dev.vardy.pagination.Page;
import dev.vardy.pagination.Pagination;
import dev.vardy.reminder.Reminder;
import dev.vardy.reminder.ReminderHandler;
import net.dv8tion.jda.core.EmbedBuilder;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
