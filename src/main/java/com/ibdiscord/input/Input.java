package com.ibdiscord.input;

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
public interface Input {

    /**
     * Gets the succeeding input.
     * @return The successor.
     */
    Input getSuccessor();

    /**
     * Gets the timeout duration for the input, in milliseconds.
     * @return The timeout as a long.
     */
    long getTimeout();

    /**
     * Initializes the input.
     * @param message The initializing message.
     */
    void initialize(Message message);

    /**
     * Offers the input a certain string of inputted text.
     * @param message The message.
     * @return True if the input was accepted, false if it was rejected.
     */
    boolean offer(Message message);

}
