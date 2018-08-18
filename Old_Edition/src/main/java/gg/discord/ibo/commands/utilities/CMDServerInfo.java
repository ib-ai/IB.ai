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

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

/**
 * @author pants
 * @since 2018.02.17
 */
 
/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

public class CMDServerInfo extends CommandAbstract{

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

        DateTimeFormatterBuilder dateTimeFormatterBuilder = new DateTimeFormatterBuilder();
        dateTimeFormatterBuilder.appendPattern("EEE, yyyy.MM.dd, hh:mm a");
        DateTimeFormatter dateTimeFormatter = dateTimeFormatterBuilder.toFormatter();

        String vip = "";
        int onlineMemberCount = guild.getMembers().size();
        int botMembers = 0;
        for(Member member : guild.getMembers()){
            if((member.getOnlineStatus() == OnlineStatus.OFFLINE)
                || (member.getOnlineStatus() == OnlineStatus.INVISIBLE)){

                onlineMemberCount--;
            }

            if(member.getUser().isBot()){
                botMembers++;
            }
        }

        try {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(Color.white);
            embedBuilder.setAuthor(guild.getName(), "https://discord.me/pbh", guild.getIconUrl());
            embedBuilder.addField("ID", guild.getId(), true);
            embedBuilder.addField("Owner", guild.getOwner().getUser().getName() + "#" + guild.getOwner().getUser().getDiscriminator(), true);
            embedBuilder.addField("Creation Date", guild.getCreationTime().format(dateTimeFormatter), true);
            embedBuilder.addField("Server Region", guild.getRegion().getName() + " " + vip, true);
            embedBuilder.addField("# of Members", String.valueOf(guild.getMembers().size()), true);
            embedBuilder.addField("# of Bots", String.valueOf(botMembers), true);
            embedBuilder.addField("Currently Online", String.valueOf(onlineMemberCount), true);
            embedBuilder.addField("# of Roles", String.valueOf(guild.getRoles().size()), true);
            embedBuilder.addField("# of Channels", String.valueOf(guild.getTextChannels().size() + guild.getVoiceChannels().size()), true);
            event.getChannel().sendMessage(embedBuilder.build()).queue();

        }catch(Exception ex){
            ex.printStackTrace();
        }
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