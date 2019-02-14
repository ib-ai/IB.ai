package com.ibdiscord.command.commands;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.HashSet;
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
