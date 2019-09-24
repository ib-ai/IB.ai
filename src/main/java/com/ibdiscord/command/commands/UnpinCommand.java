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
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.Role;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class UnpinCommand extends Command {

    /**
     * Creates a new Pin command.
     */
    public UnpinCommand() {
        super("unpin",
                CommandPermission.discord(),
                Set.of()
        );

        this.correctUsage = "unpin <messageID>";
    }

    @Override
    protected void execute(CommandContext context) {
        Role helperRole;
        try {
            helperRole = context.getGuild().getRolesByName("Helper", true).get(0);
        } catch(IndexOutOfBoundsException ex) {
            context.reply(__(context, "error.helper_exists"));
            return;
        }
        if(helperRole == null) {
            context.reply(__(context, "error.generic"));
            return;
        }
        if(!context.getMember().getRoles().contains(helperRole)) {
            context.reply(__(context, "error.helper_perms"));
            return;
        }

        if(context.getArguments().length == 0) {
            sendUsage(context);
            return;
        }

        OffsetDateTime channelInception = context.getChannel().getTimeCreated();
        OffsetDateTime current = Instant.now().atOffset(ZoneOffset.UTC);
        long id = context.getChannel().getLatestMessageIdLong();
        while(current.isAfter(channelInception)) {
            MessageHistory messageHistory = context.getChannel().getHistoryBefore(id, 100).complete();
            List<Message> history = messageHistory.getRetrievedHistory();
            if(history.isEmpty()) {
                break;
            }
            Message updated = history.get(history.size() - 1);
            current = updated.getTimeCreated();
            id = updated.getIdLong();
            List<Message> matchingMessages =  messageHistory.getRetrievedHistory().stream()
                    .filter(msg -> msg.getId()
                            .equals(context.getArguments()[0]))
                    .collect(Collectors.toList());
            if(matchingMessages.size() > 0) {
                matchingMessages.get(0).unpin().queue();
                context.reply(__(context, "success.done"));
                return;
            }
        }
        context.reply(__(context, "error.pin_channel"));
    }
}
