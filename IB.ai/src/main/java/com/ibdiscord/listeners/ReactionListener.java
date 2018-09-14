/*******************************************************************************
 * Copyright 2018 pants
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.ibdiscord.listeners;

import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/**
 * @author pants
 * @since 2018.09.14
 */

public class ReactionListener extends ListenerAdapter {

    /** <p>Triggered when a reaction is added from a message in a private chat,
     * private group DM or within a guild. <br>
     * Uses a case to filter the event down to a single group type.</p>
     */
    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        // Reaction added to message
        switch(event.getChannelType()) {
            case GROUP:
            break;

            case PRIVATE:
            break;

            case TEXT:
            break;
        }
    }

    /** <p>Triggered when a reaction is removed from a message in a private chat,
     * private group DM or within a guild. <br>
     * Uses a case to filter the event down to a single group type.</p>
     */
    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
        switch(event.getChannelType()) {
            case GROUP:
                break;

            case PRIVATE:
                break;

            case TEXT:
                break;
        }
    }
}
