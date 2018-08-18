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

import gg.discord.ibo.configuration.Configuration;
import gg.discord.ibo.redis.Redis;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.io.File;
import java.io.IOException;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

/**
 * @author pants
 * @since 2018.02.17
 */
 
/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

public class JoinListener extends ListenerAdapter{

    public void onGuildMemberJoin(GuildMemberJoinEvent event){

        String botPrefix = Redis.getInstance().getGuildPrefix(event.getGuild());
        TextChannel welcomeChannel = null;
        try{
            welcomeChannel = event.getGuild().getTextChannelById(Configuration.getInstance().getWelcomeChannelID());

        }catch(IOException ex){
            ex.printStackTrace();
        }

        if(welcomeChannel == null){
            return;
        }

        EmbedBuilder ebJoinMessage = new EmbedBuilder();
        MessageBuilder message = new MessageBuilder();

        ebJoinMessage.setColor(Color.white);
        ebJoinMessage.setAuthor("IB-Bot", "https://discord.me/pbh", null);
        ebJoinMessage.setDescription("**Welcome " + event.getUser().getAsMention() + "! IBO Subreddit: https://reddit.com/r/IBO/** " +
                "\n");

        ebJoinMessage.addField("How to gain access to the server:",
                "  1) Read the rules in #rules" +
                "\n 2) Verify yourself by typing the command `"+ botPrefix +"verify` into this channel!" +
                "\n 3) Add roles for your exam session and the subjects you take in #role-spam. Available exam sessions:" +
                "\n`May 2020, November 2019, May 2019, November 2018` `Alumni, Pre-IB, Not IB`", false);

        ebJoinMessage.addField("Joining and leaving roles:", "" +
                "\n`" + botPrefix + "Join [Role Name]` and `" + botPrefix + "leave [Role Name]`" +
                "\nFor example: `" + botPrefix + "join geo` to see the geography channels." +
                "\nand `" + botPrefix + "Join November 2018` if your exams are in November this year.", false);

        ebJoinMessage.addField("Further information::", "" +
                "\nType `" + botPrefix + "Help` and/or `" + botPrefix + "CommandList`" +
                "\nIf you have troubles with the mobile verification or this process, please direct-message a moderator or admin.", false);

        File welcomeGIF = new File("WelcomeGIF.gif");
        message.setEmbed(ebJoinMessage.build());
        welcomeChannel.sendFile(welcomeGIF, message.build()).queue();
    }
}