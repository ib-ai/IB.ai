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
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

/**
 * @author pants
 * @since 2018.02.17
 */
 
/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

public class CMDUserInfo extends CommandAbstract{
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

        //Piecing together the username of the user subject to userinfo.
        if(args.length != 0){

            //Checks if it was an ID that was submitted
            try{
                guild.getMemberById(args[0]);
                user = guild.getMemberById(args[0]).getUser();

            }catch(Exception ex){
                if(!(message.getMentionedUsers().isEmpty())){
                    user = message.getMentionedUsers().get(0);

                //Treats it as a username
                }else{
                    StringBuilder userNameBuilder = new StringBuilder();
                    for(int i = 0; i < args.length; i++){
                        if(i+1 == args.length){
                            userNameBuilder.append(args[i]);
                            break;
                        }

                        userNameBuilder.append(args[i]);
                        userNameBuilder.append(" ");
                    }
                    String userName = userNameBuilder.toString();

                    if(guild.getMembersByName(userName, true).isEmpty()){
                        event.getChannel().sendMessage("\"" + userName + "\" is an invalid username. Try the user's ID or @mention them.").queue();
                        return;
                    }

                    user = guild.getMembersByName(userName, true).get(0).getUser();
                }
            }
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();

        DateTimeFormatterBuilder dateTimeFormatterBuilder = new DateTimeFormatterBuilder();
        dateTimeFormatterBuilder.appendPattern("EEE, yyyy.MM.dd, hh:mm a");
        DateTimeFormatter dateTimeFormatter = dateTimeFormatterBuilder.toFormatter();

        String userNickname;
        if(guild.getMember(user).getNickname() != null){
            userNickname = guild.getMember(user).getNickname();
        }else{
            userNickname = "N/A";
        }

        String userGame;
        if(guild.getMember(user).getGame() != null){
            userGame = guild.getMember(user).getGame().getName();
        }else{
            userGame = "Not playing";
        }

        embedBuilder.setColor(Color.white);
        embedBuilder.setAuthor(user.getName() + "#" + user.getDiscriminator(), "https://discord.me/pbh", user.getAvatarUrl());
        embedBuilder.addField("ID", user.getId(), true);
        embedBuilder.addField("Nickname", userNickname, true);
        embedBuilder.addField("Status", guild.getMember(user).getOnlineStatus().toString(), true);
        embedBuilder.addField("Game", userGame, true);
        embedBuilder.addField("Joined", guild.getMember(user).getJoinDate().format(dateTimeFormatter), true);

        //Orders list of members by join date.
        List<Member> membersOfGuild = new ArrayList<>();
        membersOfGuild.addAll(guild.getMembers());
        Collections.sort(membersOfGuild, new Comparator<Member>(){
            @Override
            public int compare(Member mem1, Member mem2) {

                //Ascending by difference between times since epoch of join dates.
                return Math.toIntExact(mem1.getJoinDate().toEpochSecond() - mem2.getJoinDate().toEpochSecond());
            }

        });

        //Find actual join position of member.
        int joinPosition = 0;
        for(Member member : membersOfGuild){
            joinPosition++;
            if(member.getUser() == user){
                break;
            }
        }

        embedBuilder.addField("Join Position", String.valueOf(joinPosition), true);
        embedBuilder.addField("Registered", user.getCreationTime().format(dateTimeFormatter), false);

        event.getChannel().sendMessage(embedBuilder.build()).queue();
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