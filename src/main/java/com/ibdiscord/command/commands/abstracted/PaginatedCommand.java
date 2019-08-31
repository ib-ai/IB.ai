package com.ibdiscord.command.commands.abstracted;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.pagination.Page;
import com.ibdiscord.pagination.Pagination;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Set;

/**
 * Copyright 2017-2019 Arraying
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
    @SuppressWarnings("SameParameterValue")
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
