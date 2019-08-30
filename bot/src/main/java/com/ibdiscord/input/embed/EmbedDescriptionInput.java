package com.ibdiscord.input.embed;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

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
