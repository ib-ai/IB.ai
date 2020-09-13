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

package com.ibdiscord.command.actions;

import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.abstractions.PaginatedCommand;
import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.cassowary.CassowariesData;
import com.ibdiscord.data.db.entries.cassowary.CassowaryData;
import com.ibdiscord.data.db.entries.cassowary.CassowaryPenguinData;
import com.ibdiscord.pagination.Page;
import com.ibdiscord.pagination.Pagination;
import de.arraying.gravity.data.property.Property;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;

import java.util.List;
import java.util.stream.Collectors;

public final class CassowaryList extends PaginatedCommand<String> {

    @Override
    protected Pagination<String> getPagination(CommandContext context) {
        List<String> values = DataContainer.INSTANCE.getGravity().load(new CassowariesData(
                context.getGuild().getId()
        )).values()
                .stream()
                .map(Property::asString)
                .collect(Collectors.toList());

        return new Pagination<>(values, 15);
    }

    @Override
    protected void handle(CommandContext context, EmbedBuilder embedBuilder, Page<String> page) {
        CassowaryPenguinData cassowaryPenguins = DataContainer.INSTANCE.getGravity().load(new CassowaryPenguinData(context.getGuild().getId()));

        String cas = page.getValue();
        boolean penguin = cassowaryPenguins.getKeys().contains(cas.toLowerCase());
        String anchorID = penguin ? cassowaryPenguins.get(cas.toLowerCase()).asString() : "";

        String key = String.format("%s%s", cas, penguin ? " [penguin]" : "");

        String roles = DataContainer.INSTANCE.getGravity().load(new CassowaryData(context.getGuild().getId(), cas))
                .values()
                .stream()
                .map(Property::asString)
                .map(id -> {
                    String formatted = id;

                    Role role = context.getGuild().getRoleById(id);
                    if (role != null) {
                        formatted = String.format("%s (%s)", formatted, role.getName());
                    }

                    if (id.equals(anchorID)) {
                        formatted = String.format("**%s**", formatted);
                    }

                    return formatted;
                })
                .collect(Collectors.joining(", "));

        embedBuilder.addField(key, roles, false);
    }

    @Override
    protected void tweak(CommandContext context, EmbedBuilder embedBuilder) {
        embedBuilder.setDescription(__(context, "info.list_cassowary")); // Can't use i18n embed builder here.
    }

}
