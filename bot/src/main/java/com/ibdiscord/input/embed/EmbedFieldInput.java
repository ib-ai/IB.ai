package com.ibdiscord.input.embed;

import com.ibdiscord.utils.UInput;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.util.List;

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
