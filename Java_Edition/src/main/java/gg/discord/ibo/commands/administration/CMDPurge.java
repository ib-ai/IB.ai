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
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

/**
 * @author pants
 * @since 2018.03.25
 */
 
/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

public class CMDPurge extends CommandAbstract {

    @Override
    public boolean safe(GuildMessageReceivedEvent event,
                        Guild guild,
                        User user) {
        try {
            return (guild.getMember(user).getPermissions().contains(Permission.MESSAGE_MANAGE))
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
                        String raw) {

        User userToFilter = null;
        int checkAmount = 0;

        if (args.length != 1 && args.length != 2) {
            event.getChannel().sendMessage("Correct usage: `" + Redis.getInstance().getGuildPrefix(guild) +
                    "Purge <Number of Messages>` or `" + Redis.getInstance().getGuildPrefix(guild) + "Purge [@user] <Number of Messages>` or `"
                    + Redis.getInstance().getGuildPrefix(guild) + "Purge [UserID] <Number of Messages>`").queue();
            return;
        }

        if(args.length == 1){
            if (!isInteger(args[0])) {
                event.getChannel().sendMessage("Correct usage: `" + Redis.getInstance().getGuildPrefix(guild) +
                        "Purge <Number of Messages>` or `" + Redis.getInstance().getGuildPrefix(guild) + "Purge [@user] <Number of Messages>` or `"
                        + Redis.getInstance().getGuildPrefix(guild) + "Purge [UserID] <Number of Messages>`").queue();
                return;
            }

            checkAmount = Integer.parseInt(args[0]);

        }else{
            try{
                guild.getMemberById(args[0]);

            }catch(Exception ex){
                if(message.getMentionedUsers().isEmpty()){
                    event.getChannel().sendMessage("Correct usage: `" + Redis.getInstance().getGuildPrefix(guild) +
                            "Purge <Number of Messages>` or `" + Redis.getInstance().getGuildPrefix(guild) + "Purge [@user] <Number of Messages>` or `"
                            + Redis.getInstance().getGuildPrefix(guild) + "Purge [UserID] <Number of Messages>`").queue();
                    return;
                }
            }

            if(!isInteger(args[1])){
                event.getChannel().sendMessage("Correct usage: `" + Redis.getInstance().getGuildPrefix(guild) +
                        "Purge <Number of Messages>` or `" + Redis.getInstance().getGuildPrefix(guild) + "Purge [@user] <Number of Messages>` or `"
                        + Redis.getInstance().getGuildPrefix(guild) + "Purge [UserID] <Number of Messages>`").queue();
                return;
            }

            if(!(message.getMentionedUsers().isEmpty())){
                userToFilter = message.getMentionedUsers().get(0);
            }else{
                userToFilter = guild.getMemberById(args[0]).getUser();
            }
            checkAmount = Integer.parseInt(args[1]);
        }

        if(checkAmount > 100){
            event.getChannel().sendMessage("You can not purge more than 100 messages at a time!").queue();
            return;
        }


        if(userToFilter == null){

            for(Message m : event.getChannel().getIterableHistory()){
                m.delete().queue();
                if(checkAmount-- <= 0){
                    break;
                }
            }

        }else{

            int iterations = 1;
            for(Message m : event.getChannel().getIterableHistory()){
                if(m.getMember().getUser() == userToFilter){
                    m.delete().queue();
                    if(checkAmount-- <= 0){
                        break;
                    }
                }

                if(iterations > 200){
                    return;
                }
                iterations++;
            }
        }
    }

    @Override
    public void post(GuildMessageReceivedEvent event,
                     Guild guild,
                     User user,
                     Message message,
                     String[] args,
                     String raw) {
        ///
    }

    @Override
    public void onPermissionFailure(GuildMessageReceivedEvent event,
                                    Guild guild,
                                    User user,
                                    Message message,
                                    String[] args,
                                    String raw) {

        event.getChannel().sendMessage("This command requires the `Manage Messages` permission. " + user.getAsMention()).queue();
    }

    private static boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
        } catch (NumberFormatException | NullPointerException ex) {
            return false;
        }
        return true;
    }
}