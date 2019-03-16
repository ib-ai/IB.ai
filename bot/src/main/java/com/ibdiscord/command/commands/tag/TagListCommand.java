package com.ibdiscord.command.commands.tag;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.DContainer;
import com.ibdiscord.data.db.entries.TagData;
import com.ibdiscord.pagination.Pagination;
import com.ibdiscord.utils.UString;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;

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
public final class TagListCommand extends Command {

    /**
     * Creates the command.
     */
    TagListCommand() {
        super("list",
                new HashSet<>(),
                CommandPermission.discord(Permission.MANAGE_CHANNEL),
                new HashSet<>());
    }

    /**
     * Lists all tags.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        TagData tagData = DContainer.INSTANCE.getGravity().load(new TagData(context.getGuild().getId()));
        int page = 1;
        if(context.getArguments().length > 0) {
            try {
                page = Integer.valueOf(context.getArguments()[0]);
            } catch(IllegalArgumentException ignored) {}
        }
        List<String> entries = tagData.getKeys().stream()
                .sorted(String::compareToIgnoreCase)
                .collect(Collectors.toList());
        Pagination<String> pagination = new Pagination<>(entries, 10);
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setDescription("Here is a list of tags.")
                .setFooter("Page " + page + "/" + pagination.total(), null);
        pagination.page(page)
                .forEach(entry -> {
                    String value = tagData.get(entry.getValue()).asString();
                    embedBuilder.addField(UString.escapeFormatting(entry.getValue()), value, false);
                });
        context.reply(embedBuilder.build());
    }
}
