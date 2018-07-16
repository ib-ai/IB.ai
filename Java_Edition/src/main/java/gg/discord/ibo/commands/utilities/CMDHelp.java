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
package gg.discord.ibo.commands.utilities;

import gg.discord.ibo.commands.CommandAbstract;

import gg.discord.ibo.configuration.Configuration;
import gg.discord.ibo.redis.Redis;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.io.IOException;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

/**
 * @author pants
 * @since 2018.01.08
 */
 
/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

public class CMDHelp extends CommandAbstract{
    public CMDHelp(){}

    @Override
    public boolean safe(GuildMessageReceivedEvent event,
                        Guild guild,
                        User user){
        return true;
    }

    @Override
    public void execute(GuildMessageReceivedEvent event,
                        Guild guild,
                        User user,
                        Message message,
                        String[] args,
                        String raw){

        String botPrefix = Redis.getInstance().getGuildPrefix(guild);
        EmbedBuilder ebHelpMenu = new EmbedBuilder();
        try{
            ebHelpMenu.setColor(Color.white);
            ebHelpMenu.setAuthor("IB-Bot", "https://discord.me/pbh", null);
            ebHelpMenu.setDescription("Hey! Welcome to the IBO Discord Server. I'm IB-Bot version: `" + Configuration.getInstance().getBotVersion() + "` Here's some of the things I can do:");
            ebHelpMenu.addField("Getting Started:", "You can join subjects by typing: `" + botPrefix + "Join [SubjectName]`, which will grant you access to its subject-specific channels. " +
                    "\n \nWhich subjects are available? See that by typing: `" + botPrefix + "SubjectList`." +
                    "\n \nIf you want to leave a role you can type: `" + botPrefix + "Leave [SubjectName]`." +
                    "\n \nTo get your final-exam-year flair, type: `" + botPrefix + "Join [Month Year]` This'll also give you a fancy coloured name.", false);
            ebHelpMenu.addField("IB Resources:", "You can go to the IB Resources website to get up-to-date download links for textbooks, past papers, exam guides and more! Type: " +
                    "\n`link the resources`", false);
            ebHelpMenu.addField("Other Commands:","- Server details, including member count: `" + botPrefix + "ServerInfo`" +
                    "\n- Details of a user: `" + botPrefix + "UserInfo [User Name]`" +
                    "\n- Bot's Ping: `" + botPrefix + "Ping`" +
                    "\n- See this help menu: `" + botPrefix + "Help`",false);
            ebHelpMenu.addField("Information", "- Developed by [pants](http://github.com/pants1/ib-bot)", false);
        }catch(IOException ex){
            ex.printStackTrace();
        }

        event.getChannel().sendMessage(ebHelpMenu.build()).queue();
    }

    @Override
    public void post(GuildMessageReceivedEvent event,
                     Guild guild,
                     User user,
                     Message message,
                     String[] args,
                     String raw){
        ///
    }

    @Override
    public void onPermissionFailure(GuildMessageReceivedEvent event,
                                    Guild guild,
                                    User user,
                                    Message message,
                                    String[] args,
                                    String raw){
        ///
    }

}