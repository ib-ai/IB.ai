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
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.io.IOException;
import java.util.Objects;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

/**
 * @author pants
 * @since 2018.02.17
 */
 
/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

public class CMDKick extends CommandAbstract {
    @Override
    public boolean safe(GuildMessageReceivedEvent event,
                        Guild guild,
                        User user){

        try{
            return (guild.getMember(user).getPermissions().contains(Permission.KICK_MEMBERS))
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

        try{
            guild.getMemberById(args[0]);
        }catch(Exception ex){
            if(message.getMentionedUsers().isEmpty()){
                event.getChannel().sendMessage("Correct usage: `" + Redis.getInstance().getGuildPrefix(guild) + "Kick @User` or `"
                        + Redis.getInstance().getGuildPrefix(guild) + "Kick [UserID]`").queue();
                return;
            }
        }

        User userToKick;
        if(!(message.getMentionedUsers().isEmpty())){
            userToKick = message.getMentionedUsers().get(0);

        }else{
            userToKick = guild.getMemberById(args[0]).getUser();
        }


        //Actually kicks the user.
        guild.getController().kick(userToKick.getId()).complete();

        if(!Redis.getInstance().getGuildModLogID(guild).equals("000")){
            //Case number to be used in the message sent to the modlog.
            String caseNumber = String.valueOf(Redis.getInstance().getCaseNumber(guild));

            //Setting up user info for the offender.
            String kickedUserName = userToKick.getName() + "#" + userToKick.getDiscriminator();
            String kickedUserID = userToKick.getId();

            //Setting up user info for the moderator who sent the message.
            String modUserName = user.getName() + "#" + user.getDiscriminator();
            String modUserID = user.getId();

            //Sends message to the modlog using the caseNumber, banned user and moderator's information.
            Long modLogChannelID = Long.parseLong(Redis.getInstance().getGuildModLogID(guild));

            guild.getTextChannelById(modLogChannelID).sendMessage(
                    "**Case: #" + caseNumber + " | Kick :boot:**\n" +
                            "**Offender: **" + kickedUserName + " (ID: " + kickedUserID + ")\n" +
                            "**Moderator: **" + modUserName + " (ID: " + modUserID + ")\n" +
                            "**Reason: ** Use `" + Redis.getInstance().getGuildPrefix(guild) + "Reason [Case Number]` to append a reason.")
                    .queue(owo -> Redis.getInstance().addUserBan(guild, kickedUserID, caseNumber, owo.getId()));

            //Record the ban to Redis, i.e: "001||user#0001||1234567890"
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

        event.getChannel().sendMessage("This command requires the `Kick Members` permission. " + user.getAsMention()).queue();
    }
}