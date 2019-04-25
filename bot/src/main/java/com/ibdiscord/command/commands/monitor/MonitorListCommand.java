package com.ibdiscord.command.commands.monitor;

import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.commands.abstracted.PaginatedCommand;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.DContainer;
import com.ibdiscord.data.db.entries.monitor.MonitorMessageData;
import com.ibdiscord.data.db.entries.monitor.MonitorUserData;
import com.ibdiscord.pagination.Page;
import com.ibdiscord.pagination.Pagination;
import de.arraying.gravity.Gravity;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Copyright 2019 Arraying
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
public final class MonitorListCommand extends PaginatedCommand<String> {

    /**
     * Creates the command.
     */
    MonitorListCommand() {
        super("list",
                Set.of(),
                CommandPermission.discord(),
                Set.of()
        );
    }

    /**
     * Gets all the monitor thingies.
     * @param context The command context.
     * @return The pagination.
     */
    @Override
    protected Pagination<String> getPagination(CommandContext context) {
        Gravity gravity = DContainer.INSTANCE.getGravity();
        List<String> a = gravity.load(new MonitorUserData(context.getGuild().getId())).values().stream()
                .map(it -> "User: " + it)
                .collect(Collectors.toList());
        List<String> b = gravity.load(new MonitorMessageData(context.getGuild().getId())).values().stream()
                .map(it -> "Regex: " + it)
                .collect(Collectors.toList());
        a.addAll(b);
        return new Pagination<>(a, 10);
    }

    /**
     * Adds the monitored entity.
     * @param context The context.
     * @param embedBuilder The embed builder.
     * @param page The page.
     */
    @Override
    protected void handle(CommandContext context, EmbedBuilder embedBuilder, Page<String> page) {
        embedBuilder.addField("Entry #" + page.getNumber(), page.getValue(), false);
    }

    /**
     * Adds a description.
     * @param context The context.
     * @param embedBuilder The embed builder.
     */
    @Override
    protected void tweak(CommandContext context, EmbedBuilder embedBuilder) {
        embedBuilder.setDescription("Here is a list of entries.");
    }

}
