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
package gg.discord.ibo.listeners.parse;

import gg.discord.ibo.commands.Commands;
import gg.discord.ibo.redis.Redis;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

/**
 * @author pants
 * @since 2018.01.19
 */

/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

public class Parse{
    public static void generateMessage(GuildMessageReceivedEvent event){
        String[] args;
        String invoke;

        //Processing raw string into tokens split by spaces in message.
        String prefixRemoved = event.getMessage().getRawContent()
                .replaceFirst(Redis.getInstance().getGuildPrefix(event.getGuild()),"");

        String[] splitAtSpaces = prefixRemoved.split(" ");
        ArrayList<String> split = new ArrayList<>();
        split.addAll(Arrays.asList(splitAtSpaces));

        //First index of array is used as invoker, rest are considered arguments.
        invoke = split.get(0).toLowerCase();

        args = new String[split.size()-1];
        split.subList(1, split.size()).toArray(args);

        Commands.handleCommand(event,
                event.getGuild(),
                event.getAuthor(),
                event.getMessage(),
                args,
                invoke,
                event.getMessage().getRawContent());
    }
}
