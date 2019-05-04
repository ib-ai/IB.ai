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

import dev.vardy.command.CommandContext;
import dev.vardy.command.commands.abstracted.PaginatedCommand;
import dev.vardy.command.permissions.CommandPermission;
import dev.vardy.data.db.DContainer;
import dev.vardy.data.db.entries.monitor.MonitorMessageData;
import dev.vardy.data.db.entries.monitor.MonitorUserData;
import dev.vardy.pagination.Page;
import dev.vardy.pagination.Pagination;
import de.arraying.gravity.Gravity;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class MonitorListCommand extends PaginatedCommand<String> {

    /**
     * Creates the command.
     */
    MonitorListCommand() {
        super("list",
                Set.of(),
                CommandPermission.discord(),
                Set.of()
        );
    }

    /**
     * Gets all the monitor thingies.
     * @param context The command context.
     * @return The pagination.
     */
    @Override
    protected Pagination<String> getPagination(CommandContext context) {
        Gravity gravity = DContainer.INSTANCE.getGravity();
        List<String> a = gravity.load(new MonitorUserData(context.getGuild().getId())).values().stream()
                .map(it -> "User: " + it)
                .collect(Collectors.toList());
        List<String> b = gravity.load(new MonitorMessageData(context.getGuild().getId())).values().stream()
                .map(it -> "Regex: " + it)
                .collect(Collectors.toList());
        a.addAll(b);
        return new Pagination<>(a, 10);
    }

    /**
     * Adds the monitored entity.
     * @param context The context.
     * @param embedBuilder The embed builder.
     * @param page The page.
     */
    @Override
    protected void handle(CommandContext context, EmbedBuilder embedBuilder, Page<String> page) {
        embedBuilder.addField("Entry #" + page.getNumber(), page.getValue(), false);
    }

    /**
     * Adds a description.
     * @param context The context.
     * @param embedBuilder The embed builder.
     */
    @Override
    protected void tweak(CommandContext context, EmbedBuilder embedBuilder) {
        embedBuilder.setDescription("Here is a list of entries.");
    }

}
