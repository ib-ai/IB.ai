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
import com.ibdiscord.data.db.entries.helper.HelperMessageData;
import com.ibdiscord.data.db.entries.helper.HelperMessageRolesData;
import com.ibdiscord.pagination.Page;
import com.ibdiscord.pagination.Pagination;
import com.ibdiscord.utils.UString;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;
import java.util.stream.Collectors;

public final class HelperMessageList extends PaginatedCommand<String> {

    /**
     * Gets the tags as a pagination.
     * @param context The command context.
     * @return The pagination.
     */
    @Override
    protected Pagination<String> getPagination(CommandContext context) {
        HelperMessageRolesData helperMessageRolesData = DataContainer.INSTANCE.getGravity().load(
                new HelperMessageRolesData(context.getGuild().getId())
        );
        List<String> entries = helperMessageRolesData.values()
                .stream()
                .map(property -> property.asString())
                .sorted()
                .collect(Collectors.toList());
        return new Pagination<>(entries, 10);
    }

    /**
     * Handles the page.
     * @param context The context.
     * @param embedBuilder The embed builder.
     * @param page The page.
     */
    @Override
    protected void handle(CommandContext context, EmbedBuilder embedBuilder, Page<String> page) {
        HelperMessageData helperMessageData = DataContainer.INSTANCE.getGravity().load(
                new HelperMessageData(context.getGuild().getId(), page.getValue())
        );

        Role role = context.getGuild().getRoleById(page.getValue());

        String name = "???";
        if (role != null) {
            name = role.getName();
        }

        String key = String.format("%s (%s)", name, page.getValue());

        StringBuilder channels = new StringBuilder();
        helperMessageData.getKeys()
                .stream()
                .map(channelId -> {
                    TextChannel textChannel = context.getGuild().getTextChannelById(channelId);
                    if (textChannel == null) {
                        return channelId;
                    } else {
                        return textChannel.getAsMention();
                    }
                })
                .sorted(String::compareToIgnoreCase)
                .forEach(channel -> channels.append(channel).append(", "));

        String value = UString.truncate(channels.substring(0, channels.length() - 2), 512);

        embedBuilder.addField(key, value, false);
    }

    /**
     * Adds a description.
     * @param context The context.
     * @param embedBuilder The embed builder.
     */
    @Override
    protected void tweak(CommandContext context, EmbedBuilder embedBuilder) {
    }

}
