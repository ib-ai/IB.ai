package com.ibdiscord.command.commands.monitor;

import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.commands.abstracted.PaginatedCommand;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.monitor.MonitorMessageData;
import com.ibdiscord.data.db.entries.monitor.MonitorUserData;
import com.ibdiscord.localisation.Localiser;
import com.ibdiscord.pagination.Page;
import com.ibdiscord.pagination.Pagination;
import de.arraying.gravity.Gravity;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Copyright 2017-2019 Arraying
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
        Gravity gravity = DataContainer.INSTANCE.getGravity();
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
        embedBuilder.addField(Localiser.__(context, "info.entry") + page.getNumber(), page.getValue(), false);
    }

    /**
     * Adds a description.
     * @param context The context.
     * @param embedBuilder The embed builder.
     */
    @Override
    protected void tweak(CommandContext context, EmbedBuilder embedBuilder) {
        embedBuilder.setDescription(Localiser.__(context, "info.entry_list"));
    }

}
