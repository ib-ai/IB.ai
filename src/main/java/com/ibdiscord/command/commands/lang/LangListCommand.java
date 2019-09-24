/* Copyright 2017-2019 Jarred Vardy <jarred.vardy@gmail.com>
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

package com.ibdiscord.command.commands.lang;

import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.commands.abstracted.PaginatedCommand;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.localisation.Localiser;
import com.ibdiscord.pagination.Page;
import com.ibdiscord.pagination.Pagination;
import com.ibdiscord.utils.UDatabase;
import com.ibdiscord.utils.UString;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.List;
import java.util.Set;

public final class LangListCommand extends PaginatedCommand<String> {

    /**
     * Creates a new LangList command.
     */
    LangListCommand() {
        super("lang_list",
                CommandPermission.discord(),
                Set.of()
        );
    }

    @Override
    protected Pagination<String> getPagination(CommandContext context) {
        List<String> values = Localiser.getAllLanguages();
        return new Pagination<>(values, 15);
    }

    @Override
    protected void handle(CommandContext context, EmbedBuilder embedBuilder, Page<String> page) {
        embedBuilder.addField(
                __(context, "info.number", String.valueOf(page.getNumber())),
                UString.escapeFormatting(page.getValue()),
                false
        );
    }

    @Override
    protected void tweak(CommandContext context, EmbedBuilder embedBuilder) {
        embedBuilder.setDescription(__(context, "info.supported_langs", UDatabase.getPrefix(context.getGuild())));
    }
}
