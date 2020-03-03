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
import com.ibdiscord.i18n.LocaleShorthand;
import com.ibdiscord.input.Input;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.RestAction;

@AllArgsConstructor
public final class EmbedMessageInput implements Input, LocaleShorthand {

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
    public void initialize(CommandContext context) {
        context.getChannel().sendMessage(__(context, "prompt.embed_replace")).queue();
    }

    /**
     * Offers the message.
     * @param context The context of the message.
     * @return True always.
     */
    @Override
    public boolean offer(CommandContext context) {
        RestAction<Message> result;
        try {
            long id = Long.valueOf(context.getMessage().getContentRaw());
            result = sendUpdate(channel, id);
        } catch(NumberFormatException exception) {
            result = sendNew(channel);
        }
        MessageChannel update = context.getChannel();
        result.queue(success -> update.sendMessage("Done.").queue(),
            failure -> update.sendMessage(__(context, "error.generic_suggest_perms")).queue());
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
