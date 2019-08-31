package com.ibdiscord.input.embed;

import com.ibdiscord.input.Input;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

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
public abstract class EmbedInput implements Input {

    final EmbedBuilder builder;
    Input successor;

    /**
     * Creates the embed input.
     * @param builder The builder.
     */
    EmbedInput(EmbedBuilder builder) {
        this.builder = builder;
    }

    /**
     * Internally offers the input.
     * @param input The input.
     * @return True if the input is accepted, false if it is rejected.
     */
    protected abstract boolean internalOffer(Message input);

    /**
     * Gets the successor.
     * @return The successor.
     */
    @Override
    public final Input getSuccessor() {
        return successor;
    }

    /**
     * Handles the input.
     * @param input The input.
     * @return True if the input is accepted, false if it is rejected.
     */
    @Override
    public final boolean offer(Message input) {
        switch(input.getContentRaw().toLowerCase()) {
            case "skip":
                return true;
            case "done":
                successor = new EmbedChannelInput(builder, null);
                return true;
            default:
                return internalOffer(input);
        }
    }

}
