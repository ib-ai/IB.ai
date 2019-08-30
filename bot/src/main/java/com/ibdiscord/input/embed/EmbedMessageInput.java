package com.ibdiscord.input.embed;

import com.ibdiscord.input.Input;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.RestAction;

/**
 * Copyright 2017-2019 Arraying
 * <p>
 * This file is part of IB.ai.
 * <p>
 * IB.ai is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * IB.ai is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with IB.ai. If not, see http://www.gnu.org/licenses/.
 */
public final @AllArgsConstructor class EmbedMessageInput implements Input {

    private final EmbedBuilder builder;
    private final TextChannel channel;

    /**
     * Returns nothing, this is the last step.
     * @return Null.
     */
    @Override
    public Input getSuccessor() {
        return null;
    }

    /**
     * Gets the timeout.
     * @return 30 seconds.
     */
    @Override
    public long getTimeout() {
        return 30 * 1000;
    }

    @Override
    public void initialize(Message message) {
        message.getChannel().sendMessage("Should this embed replace any existing message in that channel? If yes, type the message ID, otherwise type anything else.").queue();
    }

    /**
     * Offers the message.
     * @param message The message.
     * @return True always.
     */
    @Override
    public boolean offer(Message message) {
        RestAction<Message> result;
        try {
            long id = Long.valueOf(message.getContentRaw());
            result = sendUpdate(channel, id);
        } catch(NumberFormatException exception) {
            result = sendNew(channel);
        }
        MessageChannel update = message.getChannel();
        result.queue(success -> update.sendMessage("Done.").queue(), failure -> update.sendMessage("An error occurred, are the perms set up properly?").queue());
        return true;
    }

    /**
     * Sends a new embed.
     * @param channel The channel.
     * @return The RestAction.
     */
    private RestAction<Message> sendNew(TextChannel channel) {
        return channel.sendMessage(builder.build());
    }

    /**
     * Updates an existing message/embed.
     * @param channel The channel.
     * @param id The message ID.
     * @return The RestAction.
     */
    private RestAction<Message> sendUpdate(TextChannel channel, long id) {
        return channel.editMessageById(id, builder.build());
    }

}
