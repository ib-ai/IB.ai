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
import com.ibdiscord.data.db.entries.voting.VoteLaddersData;
import com.ibdiscord.pagination.Page;
import com.ibdiscord.pagination.Pagination;
import de.arraying.gravity.data.property.Property;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.List;
import java.util.stream.Collectors;

public final class VoteLadderList extends PaginatedCommand<String> {

    /**
     * Gets the pagination for the command context.
     * @param context The command context.
     * @return The pagination.
     */
    @Override
    protected Pagination<String> getPagination(CommandContext context) {
        List<String> ladders = DataContainer.INSTANCE.getGravity().load(new VoteLaddersData(context.getGuild().getId()))
                .values()
                .stream()
                .map(Property::asString)
                .collect(Collectors.toList());
        return new Pagination<>(ladders, 10);
    }

    /**
     * Adds the page to the embed.
     * @param context The context.
     * @param embedBuilder The embed builder.
     * @param page The page.
     */
    @Override
    protected void handle(CommandContext context, EmbedBuilder embedBuilder, Page<String> page) {
        embedBuilder.addField("#" + page.getNumber(), page.getValue(), false);
    }

    /**
     * Does last minute embed handling.
     * @param context The context.
     * @param embedBuilder The embed builder.
     */
    @Override
    protected void tweak(CommandContext context, EmbedBuilder embedBuilder) {
        embedBuilder.setDescription(__(context, "info.ladder_list"));
    }

}
