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
import com.ibdiscord.utils.UString;
import de.arraying.gravity.data.property.Property;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class CassowaryList extends PaginatedCommand<String> {

    @Override
    protected Pagination<String> getPagination(CommandContext context) {
        CassowaryPenguinData cassowaryPenguins = DataContainer.INSTANCE.getGravity()
                .load(new CassowaryPenguinData(context.getGuild().getId()));
        List<String> values = DataContainer.INSTANCE.getGravity().load(new CassowariesData(
                context.getGuild().getId()
        )).values().stream()
                .map(Property::asString)
                .map(cas -> cas + (cassowaryPenguins.values().contains(cas) ? " [penguin] " : " ") +
                        DataContainer.INSTANCE.getGravity().load(new CassowaryData(
                            context.getGuild().getId(),
                            cas
                )).values()
                        .stream()
                        // lol
                        .map(id -> id + " (" + Objects.requireNonNull(context.getGuild().getRoleById(id.asString()))
                                .getName() + ")")
                        .collect(Collectors.toSet())
                        .toString())
                .collect(Collectors.toList());
        return new Pagination<>(values, 15);
    }

    @Override
    protected void handle(CommandContext context, EmbedBuilder embedBuilder, Page<String> page) {
        embedBuilder.addField(String.format(
                "#%d", page.getNumber()), UString.escapeFormatting(page.getValue()), false
        );
    }

    @Override
    protected void tweak(CommandContext context, EmbedBuilder embedBuilder) {
        embedBuilder.setDescription(__(context, "info.list_cassowary")); // Can't use i18n embed builder here.
    }
}
