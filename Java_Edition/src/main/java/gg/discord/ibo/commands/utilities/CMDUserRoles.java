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
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

/**
 * @author pants
 * @since 2018.04.04
 */
 
/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

public class CMDUserRoles extends CommandAbstract{
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

        //Piecing together the username of the user subject to userroles.
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

        ArrayList<String> rolesByName = new ArrayList<>();
        for(Role role : guild.getMember(user).getRoles()){
            rolesByName.add(role.getName());
        }

        String output = rolesByName.toString();
        output = output.replace("[", "");
        output = output.replace("]", "");

        event.getChannel().sendMessage("This user has the following roles: `" + output + "`").queue();
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