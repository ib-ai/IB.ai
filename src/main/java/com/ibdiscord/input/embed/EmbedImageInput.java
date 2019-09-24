/* Copyright 2017-2019 Arraying
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
import net.dv8tion.jda.api.EmbedBuilder;

import java.net.MalformedURLException;
import java.net.URL;

public final class EmbedImageInput extends EmbedInput {

    /**
     * Creates the embed colour input.
     * @param builder The builder.
     */
    EmbedImageInput(EmbedBuilder builder) {
        super(builder);
    }

    /**
     * Sets the colour.
     * @param context The context of the input.
     * @return True if the colour is acceptable, otherwise false.
     */
    @Override
    protected boolean internalOffer(CommandContext context) {
        try {
            String messageRaw = context.getMessage().getContentRaw();
            new URL(messageRaw);
            builder.setImage(messageRaw);
            return true;
        } catch(MalformedURLException exception) {
            context.getChannel().sendMessage(__(context, "error.invalid_url")).queue();
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
     * @param context The initializing message's context.
     */
    @Override
    public void initialize(CommandContext context) {
        this.successor = new EmbedFieldInput(builder);
        context.getChannel().sendMessage(__(context, "prompt.embed_image")).queue();
    }
}
