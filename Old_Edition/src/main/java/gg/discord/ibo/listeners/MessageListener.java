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

/* * * * * * * * * * * * * * * * * * * * * * * * * * * */
package gg.discord.ibo.listeners;

import gg.discord.ibo.listeners.parse.Parse;
import gg.discord.ibo.redis.Redis;

import gg.discord.ibo.utils.UtilsCertifier;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

/**
 * @author pants
 * @since 2018.01.02
 */

/* Notes:
 *  Listener for new messages (PM and Guild)
 *  Starts process of filtration and command reference.
 */

/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

public class MessageListener extends ListenerAdapter{

    /**
     * Fired when a PM/group message is received by the bot, as well as text channel events.
     */
    public void onMessageReceived(MessageReceivedEvent event){
        try{
            //TODO Handle PMs, group messages and text channel messages.

        }catch(Exception ex){
            ex.printStackTrace();
        }
    }


    /**
     * Is only fired when there is a text channel event (For commands).
     */
    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        Redis.getInstance().updateGuild(event.getGuild());
        if(event.getAuthor().isBot()){
            return;
        }

        if(!UtilsCertifier.hasCertificate(event.getGuild())){
            return;
        }

        if(Redis.getInstance().getTags(event.getGuild()).containsKey(event.getMessage().getRawContent())){
            event.getChannel().sendMessage(Redis.getInstance().getTags(event.getGuild()).get(event.getMessage().getRawContent())).queue();
            return;
        }

        if(event.getMessage().getRawContent()
                .startsWith(Redis.getInstance().getGuildPrefix(event.getGuild()))){
            try{
                Parse.generateMessage(event);

            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }
}