package com.ibdiscord.input.embed;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

import java.awt.*;

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
public final class EmbedColourInput extends EmbedInput {

    /**
     * Creates the embed colour input.
     * @param builder The builder.
     */
    EmbedColourInput(EmbedBuilder builder) {
        super(builder);
    }

    /**
     * Sets the colour.
     * @param input The input.
     * @return True if the colour is acceptable, otherwise false.
     */
    @Override
    protected boolean internalOffer(Message input) {
        try {
            Color color = Color.decode(input.getContentRaw());
            builder.setColor(color);
            return true;
        } catch(Exception exception) {
            input.getChannel().sendMessage("Invalid format. Please use hex, for example: `#123456`.").queue();
            return false;
        }
    }

    /**
     * Gets the timeout.
     * @return 30 seconds.
     */
    @Override
    public long getTimeout() {
        return 30 * 1000;
    }

    /**
     * Sends the initialization.
     * @param message The initializing message.
     */
    @Override
    public void initialize(Message message) {
        this.successor = new EmbedFieldInput(builder);
        message.getChannel().sendMessage("Please provide the colour in hex format (e.g. `#123456`). Use `skip` to skip this.").queue();
    }

}
