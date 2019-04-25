package com.ibdiscord.command.commands.tag;

import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.commands.abstracted.PaginatedCommand;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.DContainer;
import com.ibdiscord.data.db.entries.TagData;
import com.ibdiscord.pagination.Page;
import com.ibdiscord.pagination.Pagination;
import com.ibdiscord.utils.UString;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Copyright 2019 Ray Clark, Arraying
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public final class TagListCommand extends PaginatedCommand<String> {

    /**
     * Creates the command.
     */
    TagListCommand() {
        super("list",
                new HashSet<>(),
                CommandPermission.discord(),
                new HashSet<>());
    }

    /**
     * Gets the tags as a pagination.
     * @param context The command context.
     * @return The pagination.
     */
    @Override
    protected Pagination<String> getPagination(CommandContext context) {
        TagData tagData = DContainer.INSTANCE.getGravity().load(new TagData(context.getGuild().getId()));
        List<String> entries = tagData.getKeys().stream()
                .sorted(String::compareToIgnoreCase)
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
        TagData tagData = DContainer.INSTANCE.getGravity().load(new TagData(context.getGuild().getId()));
        String value = tagData.get(page.getValue()).asString();
        embedBuilder.addField(UString.escapeFormatting(page.getValue()), value, false);
    }

    /**
     * Adds a description.
     * @param context The context.
     * @param embedBuilder The embed builder.
     */
    @Override
    protected void tweak(CommandContext context, EmbedBuilder embedBuilder) {
        embedBuilder.setDescription("Here is a list of tags.");
    }

}
