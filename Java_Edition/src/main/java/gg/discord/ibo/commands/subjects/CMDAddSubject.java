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
/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

import gg.discord.ibo.commands.CommandAbstract;
import gg.discord.ibo.configuration.Configuration;
import gg.discord.ibo.utils.UtilSubjects;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.io.IOException;
import java.util.Objects;

/**
 * @author pants
 * @since 2018.02.18
 */
 
/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

public class CMDAddSubject extends CommandAbstract{

    @Override
    public boolean safe(GuildMessageReceivedEvent event,
                        Guild guild,
                        User user){

        try {
            return guild.getMember(user).getPermissions().contains(Permission.MANAGE_ROLES)
                    || Objects.equals(user.getId(), Configuration.getInstance().getDevID());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void execute(GuildMessageReceivedEvent event,
                        Guild guild,
                        User user,
                        Message message,
                        String[] args,
                        String raw){
        System.out.println("Triggered");

        if(args.length > 1){
            event.getChannel().sendMessage("The subject must be one word. Sorry.").queue();
            return;
        }

        event.getChannel().sendMessage("Subject added to the SubjectList: " + args[0]).queue();
        UtilSubjects.getSubjectsJSON().array("listOfSubjects").append(args[0]);
        UtilSubjects.updateSubjectList();
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