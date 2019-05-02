package com.ibdiscord.command.commands;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.HashSet;
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
public final class PurgeCommand extends Command {

    /**
     * Creates the command
     */
    public PurgeCommand() {
        super("purge",
                Set.of("prune", "clear", "deletebrowserhistory"),
                CommandPermission.discord(Permission.MESSAGE_MANAGE),
                new HashSet<>()
        );
        this.correctUsage = "purge <amount of messages>";
    }

    /**
     * Purges the last N messages.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        if(context.getArguments().length == 0) {
            sendUsage(context);
            return;
        }
        Integer amount;
        try {
            amount = Integer.valueOf(context.getArguments()[0]);
        } catch(NumberFormatException exception) {
            context.reply("You did not specify a number.");
            return;
        }
        if(amount < 2
                || amount > 100) {
            context.reply("Amount of messages out of range (2-100).");
            return;
        }
        context.getChannel().getHistory().retrievePast(amount).queue(it ->
                ((TextChannel) context.getChannel()).deleteMessages(it).queue(jollyGood ->
                    context.reply("Consider it done."),
                bloodyHell ->
                    context.reply("I tried to hard, and got so far, but in the end, it didn't even purge.")
                )
        );
    }

}
