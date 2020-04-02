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
import com.ibdiscord.i18n.Locale;
import com.ibdiscord.i18n.LocaliserHandler;
import com.ibdiscord.pagination.Page;
import com.ibdiscord.pagination.Pagination;
import com.ibdiscord.utils.UDatabase;
import com.ibdiscord.utils.UString;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.ArrayList;
import java.util.Collection;

public final class LangList extends PaginatedCommand<Locale> {

    /**
     * Gets all languages.
     * @param context The command context.
     * @return The list.
     */
    @Override
    protected Pagination<Locale> getPagination(CommandContext context) {
        Collection<Locale> values = LocaliserHandler.INSTANCE.locales();
        return new Pagination<>(new ArrayList<>(values), 15);
    }

    /**
     * Lists all languages.
     * @param context The context.
     * @param embedBuilder The embed builder.
     * @param page The page.
     */
    @Override
    protected void handle(CommandContext context, EmbedBuilder embedBuilder, Page<Locale> page) {
        embedBuilder.addField(
                page.getValue().getName() + " " + page.getValue().getFlag(),
                UString.escapeFormatting(page.getValue().getCode()),
                true
        );
    }

    /**
     * Set description.
     * @param context The context.
     * @param embedBuilder The embed builder.
     */
    @Override
    protected void tweak(CommandContext context, EmbedBuilder embedBuilder) {
        embedBuilder.setDescription(__(context, "info.supported_langs", UDatabase.getPrefix(context.getGuild())));
    }

}
