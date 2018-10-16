/*******************************************************************************
 * Copyright 2018 Jarred Vardy
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

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.main.IBai;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import org.apache.commons.lang3.ArrayUtils;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/** @author vardy, Arraying
 * @since 2018.08.19
 */

public final class MessageListener extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if(event.getAuthor().isBot()
                || !event.getGuild().getSelfMember().hasPermission(event.getChannel(), Permission.MESSAGE_WRITE)) {
            return;
        }
        // todo user input
        String message = event.getMessage().getContentRaw();
        if(!message.startsWith(IBai.getConfig().getStaticPrefix())) {
            return;
        }
        message = message.substring(IBai.getConfig().getStaticPrefix().length()).replaceAll(" +", " ");
        String[] arguments = message.split(" ");
        String commandName = arguments[0].toLowerCase();
        Command command = Command.find(null, commandName);
        if(command != null) {
            command.preprocess(CommandContext.construct(event.getMessage(), ArrayUtils.remove(arguments, 0)));
        }
    }
}
