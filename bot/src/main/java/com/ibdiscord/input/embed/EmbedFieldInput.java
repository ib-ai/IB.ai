package com.ibdiscord.input.embed;

import com.ibdiscord.utils.UInput;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.util.List;

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
     * @param input The input.
     * @return True if accepted, false if not.
     */
    @Override
    protected boolean internalOffer(Message input) {
        List<String> quoted = UInput.extractQuotedStrings(input.getContentRaw().split(" +"));
        if(quoted.size() < 2) {
            input.getChannel().sendMessage("Please provide the field in the following form: `\"Title\" \"Value\"`").queue();
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
     * @return 1 minute.
     */
    @Override
    public long getTimeout() {
        return 60 * 1000;
    }

    /**
     * Initializes the message.
     * @param message The initial message.
     */
    @Override
    public void initialize(Message message) {
        message.getChannel().sendMessage("You are now adding another field. Please use the following format: `\"Title\" \"Value\"`. " +
                "You can type `done` to complete the embed.").queue();
        this.successor = new EmbedFieldInput(builder);
    }

}
