/* Copyright 2017-2019 Arraying
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

package com.ibdiscord.command.commands;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Set;

public final class PurgeCommand extends Command {

    /**
     * Creates the command.
     */
    public PurgeCommand() {
        super("purge",
                CommandPermission.discord(Permission.MESSAGE_MANAGE),
                Set.of()
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
            context.reply(__(context, "error.purge_number"));
            return;
        }
        if(amount < 2 || amount > 100) {
            context.reply(__(context, "error.purge_range"));
            return;
        }
        context.getChannel().getHistory().retrievePast(amount).queue(it ->
                ((TextChannel) context.getChannel()).deleteMessages(it).queue(jollyGood ->
                    context.reply(__(context, "success.done")),
                    bloodyHell ->
                        context.reply("I tried to hard, and got so far, but in the end, it didn't even purge.")
                    )
        );
    }
}
