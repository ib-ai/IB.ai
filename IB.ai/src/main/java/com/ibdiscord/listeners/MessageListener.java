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

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/** @author pants
 * @since 2018.08.19
 */

public final class MessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getAuthor().isBot()){
            return;
        }

        switch (event.getMessage().getType()) {
            case CALL: return;
            case CHANNEL_ICON_CHANGE: return;
            case CHANNEL_NAME_CHANGE: return;
            case RECIPIENT_ADD: return;
            case RECIPIENT_REMOVE: return;
            case GUILD_MEMBER_JOIN: return;
            case CHANNEL_PINNED_ADD: return;
            case DEFAULT: break;
            case UNKNOWN: break;
        }

        switch (event.getMessage().getChannel().getType()) {
            case TEXT:
                break;
            case GROUP:
                break;
            case PRIVATE:
                break;
            case UNKNOWN:
                break;
        }
    }
}
