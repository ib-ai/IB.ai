package com.ibdiscord.input;

import net.dv8tion.jda.core.entities.Message;

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
