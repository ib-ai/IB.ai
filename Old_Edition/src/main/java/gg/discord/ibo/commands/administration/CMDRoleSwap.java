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
package gg.discord.ibo.commands.administration;

import gg.discord.ibo.commands.CommandAbstract;
import gg.discord.ibo.configuration.Configuration;

import gg.discord.ibo.redis.Redis;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

/**
 * @author pants
 * @since 2018.02.17
 */
 
/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

public class CMDRoleSwap extends CommandAbstract{
    @Override
    public boolean safe(GuildMessageReceivedEvent event,
                        Guild guild,
                        User user){
        try{
            return (guild.getMember(user).getPermissions().contains(Permission.ADMINISTRATOR))
                    || Objects.equals(user.getId(), Configuration.getInstance().getDevID());

        }catch (IOException e){
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

        if(args.length < 2){
            event.getChannel().sendMessage("Correct usage: `" + Redis.getInstance().getGuildPrefix(guild) + "RoleSwap \"[Role Name From]\" \"[Role Name To]\"`").queue();
            return;
        }

        Collection<Role> rolesToRemove = new ArrayList<Role>();
        Collection<Role> rolesToAdd = new ArrayList<Role>();
        Role roleToRemove = null;

        StringBuilder argumentsBuilder = new StringBuilder();
        for(int i = 0; i < args.length; i++){
            if(i+1 == args.length){
                argumentsBuilder.append(args[i]);
                break;
            }

            argumentsBuilder.append(args[i]);
            argumentsBuilder.append(" ");
        }
        String arguments = argumentsBuilder.toString();

        Pattern argumentQuotes = Pattern.compile("(?:^|)\"([^\"]*?)\"(?:$|)");
        Matcher matcher = argumentQuotes.matcher(arguments);
        String fromRole = null;
        String toRole = null;
        boolean found = false;
        int groupNum = 0;
        while(matcher.find()){
            if(groupNum == 0){
                fromRole = matcher.group(1).replaceAll("\"", "");
            }else{
                toRole = matcher.group(1).replaceAll("\"", "");
            }
            groupNum++;
            found = true;
        }

        if(!found || fromRole == null || toRole == null){
            event.getChannel().sendMessage("Correct usage: `" + Redis.getInstance().getGuildPrefix(guild) + "RoleSwap \"[Role Name From]\" \"[Role Name To]\"`").queue();
            return;
        }

        if(!guild.getRolesByName(fromRole, true).isEmpty() &&
                !guild.getRolesByName(toRole, true).isEmpty()){

            for(Role r : guild.getRoles()){
                if(r.getName().equals(fromRole)){
                    rolesToRemove.add(r);
                    roleToRemove = r;
                }

                if(r.getName().equals(toRole)){
                    rolesToAdd.add(r);
                }
            }
        }else{
            event.getChannel().sendMessage("You probably put in an invalid role name, man. Try again.").queue();
            return;
        }

        if((rolesToRemove.isEmpty()) || (rolesToAdd.isEmpty())){
            return;
        }

        int totalChanges = 0;
        for(Member member : guild.getMembers()){
            if(member.getRoles().contains(roleToRemove)){
                guild.getController().modifyMemberRoles(member, rolesToAdd, rolesToRemove).queue();
                totalChanges++;
            }
        }
        event.getChannel().sendMessage("Total role changes made: `" + totalChanges + "`").queue();
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

        event.getChannel().sendMessage("This command requires the `Administrator` permission. " + user.getAsMention()).queue();
    }
}