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
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public final class EmbedDescriptionInput extends EmbedInput {

    /**
     * Creates a new embed builder.
     */
    public EmbedDescriptionInput() {
        super(new EmbedBuilder());
    }

    /**
     * Sets the description.
     * @param context The context of the input.
     * @return True.
     */
    @Override
    protected boolean internalOffer(CommandContext context) {
        String string = context.getMessage().getContentRaw();
        if(string.length() > MessageEmbed.TEXT_MAX_LENGTH) {
            string = string.substring(0, MessageEmbed.TEXT_MAX_LENGTH);
        }
        builder.setDescription(string);
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
     * Sends the initial message.
     * @param context The initial message's context.
     */
    @Override
    public void initialize(CommandContext context) {
        context.getChannel().sendMessage(__(context, "prompt.embed_description")).queue();
        this.successor = new EmbedColourInput(builder);
    }
}
