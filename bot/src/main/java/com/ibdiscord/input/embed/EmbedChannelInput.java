package com.ibdiscord.input.embed;

import com.ibdiscord.input.Input;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

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
public final @AllArgsConstructor class EmbedChannelInput implements Input {

    private final EmbedBuilder builder;
    private TextChannel channel;

    /**
     * @return null.
     */
    @Override
    public Input getSuccessor() {
        return new EmbedMessageInput(builder, channel);
    }

    /**
     * @return 30 seconds.
     */
    @Override
    public long getTimeout() {
        return 30 * 1000;
    }

    /**
     * Sends the message.
     * @param message The initial message.
     */
    @Override
    public void initialize(Message message) {
        message.getChannel().sendMessage("What channel should the embed be sent in?").queue();
    }

    /**
     * Handles the input.
     * @param message The message.
     * @return True if the channel is accepted, false otherwise.
     */
    @Override
    public boolean offer(Message message) {
        if(message.getMentionedChannels().isEmpty()) {
            message.getChannel().sendMessage("You need to mention a channel!").queue();
            return false;
        }
        TextChannel target = message.getMentionedChannels().get(0);
        if(!message.getMember().hasPermission(target, Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS)) {
            message.getChannel().sendMessage("You do not have access to that channel.").queue();
            return false;
        }
        channel = target;
        return true;
    }

}
