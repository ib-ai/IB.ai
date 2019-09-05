/**
 * Copyright 2017-2019 Jarred Vardy
 * <p>
 * This file is part of IB.ai.
 * <p>
 * IB.ai is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * IB.ai is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with IB.ai. If not, see http://www.gnu.org/licenses/.
 */

package com.ibdiscord.command.commands.cassowary;

import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.commands.abstracted.PaginatedCommand;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.cassowary.CassowariesData;
import com.ibdiscord.data.db.entries.cassowary.CassowaryData;
import com.ibdiscord.localisation.Localiser;
import com.ibdiscord.pagination.Page;
import com.ibdiscord.pagination.Pagination;
import com.ibdiscord.utils.UString;
import de.arraying.gravity.data.property.Property;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CassowaryList extends PaginatedCommand<String> {

    /**
     * Creates a new CassowaryList command.
     */
    CassowaryList() {
        super("list",
                Set.of("l"),
                CommandPermission.discord(Permission.MANAGE_SERVER),
                Set.of()
        );
    }

    @Override
    protected Pagination<String> getPagination(CommandContext context) {
        List<String> values = DataContainer.INSTANCE.getGravity().load(new CassowariesData()).contents().stream()
                .map(Property::asString)
                .map(cas -> cas + " " + DataContainer.INSTANCE.getGravity().load(new CassowaryData(cas)).contents().stream()
                        // lol
                        .map(id -> id + "(" + context.getGuild().getRoleById(id.asString()).getName() + ")")
                        .collect(Collectors.toSet())
                        .toString())
                .collect(Collectors.toList());
        return new Pagination<>(values, 15);
    }

    @Override
    protected void handle(CommandContext context, EmbedBuilder embedBuilder, Page<String> page) {
        embedBuilder.addField(String.format("#%d", page.getNumber()), UString.escapeFormatting(page.getValue()), false);
    }

    @Override
    protected void tweak(CommandContext context, EmbedBuilder embedBuilder) {
        embedBuilder.setDescription(Localiser.__(context, "info.list_cassowary"));
    }
}
