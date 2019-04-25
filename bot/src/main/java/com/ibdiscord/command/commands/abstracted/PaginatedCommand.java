package com.ibdiscord.command.commands.abstracted;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.pagination.Page;
import com.ibdiscord.pagination.Pagination;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.Set;

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
public abstract class PaginatedCommand<T> extends Command {

    /*
        Note to myself. You wrote this because supposedly you are for DRY friendly but then you copy and paste code.
        Use this.

     */

    /**
     * Creates a new command.
     * @param name The name of the command, all lowercase.
     * @param aliases Any aliases the command has, also all lowercase.
     * @param permission The permission required to execute the command.
     * @param subCommands Any sub commands the command has.
     */
    protected PaginatedCommand(String name, Set<String> aliases, CommandPermission permission, Set<Command> subCommands) {
        super(name, aliases, permission, subCommands);
    }


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
     * Executes the listing.
     * @param context The command context.
     */
    @Override
    protected final void execute(CommandContext context) {
        int page = 1;
        if(context.getArguments().length != 0) {
            try {
                page = Integer.valueOf(context.getArguments()[0]);
            } catch(NumberFormatException ignored) {}
        }
        Pagination<T> pagination = getPagination(context);
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setFooter("Page " + page + "/" + pagination.total(), null);
        pagination.page(page).forEach(it -> handle(context, embedBuilder, it));
        tweak(context, embedBuilder);
        context.reply(embedBuilder.build());
    }

}
