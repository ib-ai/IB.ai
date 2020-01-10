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

import com.ibdiscord.command.CommandAction;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.tag.TagData;
import com.ibdiscord.pagination.Pagination;
import com.ibdiscord.utils.UInput;
import com.ibdiscord.utils.UString;
import de.arraying.gravity.Gravity;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.List;
import java.util.stream.Collectors;

public final class TagFind implements CommandAction {

    /**
     * Finds all commands with a certain character string.
     * @param context The command context.
     */
    @Override
    public void accept(CommandContext context) {
        context.assertArguments(1, "error.generic_syntax_arg");
        String guild = context.getGuild().getId();
        String compare = UInput.extractQuotedStrings(context.getArguments()).get(0).toLowerCase();
        Gravity gravity = DataContainer.INSTANCE.getGravity();
        List<String> matches = gravity.load(new TagData(guild)).getKeys().stream()
                .map(String::toLowerCase)
                .filter(tag -> tag.contains(compare))
                .collect(Collectors.toList());
        EmbedBuilder embedBuilder = new EmbedBuilder();
        StringBuilder tags = new StringBuilder();
        Pagination<String> pagination = new Pagination<>(matches, 20);
        int page = 1;
        if(context.getArguments().length >= 2) {
            try {
                page = Integer.valueOf(context.getArguments()[1]);
            } catch (NumberFormatException ex) {
                // Ignored
            }
        }
        pagination.page(page).forEach(entry -> tags.append(UString.escapeFormatting(entry.getValue())).append(", "));
        if (tags.length() == 0) {
            embedBuilder.addField(
                    context.__(context, "error.tag_404"),
                    "",
                    false
            );
        } else {
            embedBuilder.addField(
                    context.__(context, "info.tag_list_similar"),
                    tags.substring(0, tags.length() - 2),
                    false
            );
            embedBuilder.setFooter(
                    context.__(context, "info.paginated",String.valueOf(page), String.valueOf(pagination.total())),
                    null
            );
        }
        context.replyEmbed(embedBuilder.build());
    }

}
