/* Copyright 2017-2019 Jarred Vardy <jarred.vardy@gmail.com>
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
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;

import java.util.Set;

public final class PinCommand extends Command {

    /**
     * Creates a new Pin command.
     */
    public PinCommand() {
        super("pin",
                CommandPermission.discord(),
                Set.of()
        );

        this.correctUsage = "&pin <messageID>";
    }

    @Override
    protected void execute(CommandContext context) {
        Role helperRole = context.getGuild().getRolesByName("Helper", true).get(0);
        if(!context.getMember().getRoles().contains(helperRole) || helperRole == null) {
            context.reply("You must be a helper to use this command.");
            return;
        }

        if(context.getArguments().length == 0) {
            sendUsage(context);
            return;
        }

        Message message = context.getChannel().getHistory().getMessageById(context.getArguments()[0]);
        if(message == null) {
            context.reply("Invalid message ID provided. You must be in the same channel as the message.");
        } else {
            message.pin().queue();
        }
    }
}
