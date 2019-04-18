package com.ibdiscord.command.commands.monitor;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.DContainer;
import com.ibdiscord.data.db.entries.monitor.MonitorMessageData;
import com.ibdiscord.data.db.entries.monitor.MonitorUserData;
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
public final class MonitorListCommand extends Command {

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
     * Lists all monitor entries.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        int page = 1;
        if(context.getArguments().length != 0) {
            try {
                page = Integer.valueOf(context.getArguments()[0]);
            } catch(NumberFormatException ignored) {}
        }
        Gravity gravity = DContainer.INSTANCE.getGravity();
        List<String> users = gravity.load(new MonitorUserData(context.getGuild().getId())).values().stream()
                .map(it -> "User: " + it)
                .collect(Collectors.toList());
        List<String> messages = gravity.load(new MonitorMessageData(context.getGuild().getId())).values().stream()
                .map(it -> "Regex: " + it)
                .collect(Collectors.toList());
        users.addAll(messages);
        Pagination<String> pagination = new Pagination<>(users, 10);
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setDescription("Here is a list of entries.")
                .setFooter("Page " + page + "/" + pagination.total(), null);
        pagination.page(page)
                .forEach(entry -> embedBuilder.addField("Entry #" + entry.getNumber(), entry.getValue(), false));
        context.reply(embedBuilder.build());
    }

}
