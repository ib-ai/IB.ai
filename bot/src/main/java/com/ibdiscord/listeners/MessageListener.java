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
import com.ibdiscord.data.db.DContainer;
import com.ibdiscord.data.db.entries.BotPrefixData;
import com.ibdiscord.data.db.entries.TagData;
import com.ibdiscord.main.IBai;
import de.arraying.gravity.data.property.Property;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.apache.commons.lang3.ArrayUtils;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/** @author vardy, Arraying
 * @since 2018.08.19
 */

public final class MessageListener extends ListenerAdapter {

    /**
     * When an message is sent in a guild channel (because DMs are boring).
     * @param event The event instance.
     */
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if(event.getAuthor().isBot()
                || event.getAuthor().isFake()
                || !event.getGuild().getSelfMember().hasPermission(event.getChannel(), Permission.MESSAGE_WRITE)) {
            return;
        }
        //TODO: user input
        String message = event.getMessage().getContentRaw();

        //TODO: accept wildcards by using REGEX
        //TODO: if doing this ensure to precomile the regular expression and cache it somewhere
        TagData tags = DContainer.getGravity().load(new TagData(event.getGuild().getId()));
        Property tagValueAsProperty = tags.get(message);
        if(tagValueAsProperty != null) {
            event.getChannel().sendMessage(tagValueAsProperty.toString()).queue();
        }

        //TODO: change bot prefix usage project-wide to use guild-specific prefices instead of static prefix.
        String botPrefix = IBai.getConfig().getStaticPrefix();
        try {
            botPrefix = DContainer.getGravity().load(new BotPrefixData(event.getGuild().getId())).get().toString();
        } catch(Exception e) {
            e.printStackTrace();
        }

        if(!message.startsWith(botPrefix)) {
            return;
        }
        message = message.substring(botPrefix.length()).replaceAll(" +", " ");
        String[] arguments = message.split(" ");
        String commandName = arguments[0].toLowerCase();
        Command command = Command.find(null, commandName);
        if(command != null) {
            command.preprocess(CommandContext.construct(event.getMessage(), ArrayUtils.remove(arguments, 0)));
        }
    }

    /**
     * When a message is deleted.
     * @param event The event instance.
     */
    @Override
    public void onGuildMessageDelete(GuildMessageDeleteEvent event) {
        //TODO: I don't know what to do with this, we need some form of cache to cache messages
    }

}
