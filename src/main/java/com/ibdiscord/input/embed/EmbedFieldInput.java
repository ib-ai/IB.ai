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
import com.ibdiscord.utils.UInput;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.List;

public final class EmbedFieldInput extends EmbedInput {

    /**
     * Creates a new embed field input.
     * @param builder The builder.
     */
    EmbedFieldInput(EmbedBuilder builder) {
        super(builder);
    }

    /**
     * Internally offers the string.
     * @param context The input's context.
     * @return True if accepted, false if not.
     */
    @Override
    protected boolean internalOffer(CommandContext context) {
        List<String> quoted = UInput.extractQuotedStrings(context.getMessage().getContentRaw().split(" +"));
        if(quoted.size() < 2) {
            context.getChannel().sendMessage(__(context, "error.embed_field_format")).queue();
            return false;
        }
        String field = quoted.get(0);
        String value = quoted.get(1);
        if(field.length() > MessageEmbed.TITLE_MAX_LENGTH) {
            field = field.substring(0, MessageEmbed.TITLE_MAX_LENGTH);
        }
        if(value.length() > MessageEmbed.VALUE_MAX_LENGTH) {
            value = value.substring(0, MessageEmbed.VALUE_MAX_LENGTH);
        }
        builder.addField(field, value, false);
        if(builder.getFields().size() < 20) {
            successor = new EmbedFieldInput(builder);
        }
        return true;
    }

    /**
     * Returns one minute in milliseconds as a long.
     * @return 1 minute.
     */
    @Override
    public long getTimeout() {
        return 60 * 1000;
    }

    /**
     * Initializes the message.
     * @param context The initial message's context
     */
    @Override
    public void initialize(CommandContext context) {
        context.getChannel().sendMessage(__(context, "prompt.embed_field")).queue();
        this.successor = new EmbedFieldInput(builder);
    }
}
