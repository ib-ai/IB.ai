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

package com.ibdiscord.command.abstractions;

import com.ibdiscord.command.CommandAction;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.pagination.Page;
import com.ibdiscord.pagination.Pagination;
import net.dv8tion.jda.api.EmbedBuilder;

public abstract class PaginatedCommand<T> implements CommandAction {

    /**
     * Gets all pagination data.
     * @param context The command context.
     * @return The pagination object.
     */
    protected abstract Pagination<T> getPagination(CommandContext context);

    /**
     * Handles an entry.
     * @param context The context.
     * @param embedBuilder The embed builder.
     * @param page The page.
     */
    protected abstract void handle(CommandContext context, EmbedBuilder embedBuilder, Page<T> page);

    /**
     * Tweaks the embed builder before sending the embed.
     * @param context The context.
     * @param embedBuilder The embed builder.
     */
    protected abstract void tweak(CommandContext context, EmbedBuilder embedBuilder);

    /**
     * Paginates a command.
     * @param context The command context.
     */
    @Override
    public void accept(CommandContext context) {
        int page = 1;
        if(context.getArguments().length != 0) {
            try {
                page = Integer.valueOf(context.getArguments()[0]);
            } catch(NumberFormatException ignored) {}
        }
        Pagination<T> pagination = getPagination(context);
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setFooter(context.__(context, "info.paginated",
                page,
                pagination.total()),
                null
        );
        pagination.page(page).forEach(it -> handle(context, embedBuilder, it));
        tweak(context, embedBuilder);
        context.replyEmbed(embedBuilder.build());
    }

}
