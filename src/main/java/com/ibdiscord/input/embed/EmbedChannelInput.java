/* Copyright 2018-2020 Arraying
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

package com.ibdiscord.input.embed;

import com.ibdiscord.command.CommandContext;
import com.ibdiscord.input.Input;
import com.ibdiscord.localisation.ILocalised;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Objects;

@AllArgsConstructor
public final class EmbedChannelInput implements Input, ILocalised {

    private final EmbedBuilder builder;
    private TextChannel channel;

    /**
     * Gets the successor to the current input type.
     * @return null.
     */
    @Override
    public Input getSuccessor() {
        return new EmbedMessageInput(builder, channel);
    }

    /**
     * Returns 30 seconds in milliseconds as a long.
     * @return 30 seconds.
     */
    @Override
    public long getTimeout() {
        return 30 * 1000;
    }

    /**
     * Sends the message.
     * @param context The initial message's context.
     */
    @Override
    public void initialize(CommandContext context) {
        context.getChannel().sendMessage(__(context, "prompt.embed_channel")).queue();
    }

    /**
     * Handles the input.
     * @param context The message's context.
     * @return True if the channel is accepted, false otherwise.
     */
    @Override
    public boolean offer(CommandContext context) {
        Message message = context.getMessage();
        if(message.getMentionedChannels().isEmpty()) {
            message.getChannel().sendMessage(__(context, "error.missing_channel")).queue();
            return false;
        }
        TextChannel target = message.getMentionedChannels().get(0);
        if(!Objects.requireNonNull(message.getMember())
                .hasPermission(target, Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS)) {
            message.getChannel().sendMessage(__(context, "error.missing_channel_permissions")).queue();
            return false;
        }
        channel = target;
        return true;
    }
}
