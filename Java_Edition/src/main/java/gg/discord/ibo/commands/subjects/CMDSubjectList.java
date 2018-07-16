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
package gg.discord.ibo.commands.subjects;

import gg.discord.ibo.commands.CommandAbstract;
import gg.discord.ibo.redis.Redis;
import gg.discord.ibo.utils.UtilSubjects;
import gg.discord.ibo.utils.UtilWhiteSpace;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

/**
 * @author pants
 * @since 2018.02.18
 */
 
/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

public class CMDSubjectList extends CommandAbstract{
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
        StringBuilder stringBuilder = new StringBuilder();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.white);

        UtilWhiteSpace whiteSpace = new UtilWhiteSpace(20);
        int i = 0;
        String leftColumn = null;
        String rightColumn = null;

        for(String s : UtilSubjects.getSubjects()){
            i++;
            if(i==1){
                leftColumn = s;
            }
            if(i==2){
                rightColumn = s;
                stringBuilder.append("`" + whiteSpace.s(leftColumn, rightColumn) + "`\n");
                i = 0;
                leftColumn = null;
                rightColumn = null;
            }
        }
        if(leftColumn != null){
            stringBuilder.append("`" + leftColumn + "`\n");
        }

        stringBuilder.append(" \n").append("Type `" + Redis.getInstance().getGuildPrefix(guild) + "join [Subject Name]` to get added to a subject group!");
        embedBuilder.addField("All Subjects:", stringBuilder.toString(), false);

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