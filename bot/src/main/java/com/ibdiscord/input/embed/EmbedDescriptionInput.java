package com.ibdiscord.input.embed;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;

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
public final class EmbedDescriptionInput extends EmbedInput {

    /**
     * Creates a new embed builder.
     */
    public EmbedDescriptionInput() {
        super(new EmbedBuilder());
    }

    /**
     * Sets the description.
     * @param input The input.
     * @return True.
     */
    @Override
    protected boolean internalOffer(Message input) {
        String string = input.getContentRaw();
        if(string.length() > MessageEmbed.TEXT_MAX_LENGTH) {
            string = string.substring(0, MessageEmbed.TEXT_MAX_LENGTH);
        }
        builder.setDescription(string);
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
     * Sends the initial message.
     * @param message The initial message.
     */
    @Override
    public void initialize(Message message) {
        message.getChannel().sendMessage("Please type the description of the embed. Use `skip` to skip this.").queue();
        this.successor = new EmbedColourInput(builder);
    }

}
