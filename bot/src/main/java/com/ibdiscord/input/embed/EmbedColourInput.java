package com.ibdiscord.input.embed;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

import java.awt.*;

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
        this.successor = new EmbedImageInput(builder);
        message.getChannel().sendMessage("Please provide the colour in hex format (e.g. `#123456`). Use `skip` to skip this.").queue();
    }

}
