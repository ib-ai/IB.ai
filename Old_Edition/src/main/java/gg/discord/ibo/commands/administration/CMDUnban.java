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
 * @since 2018.02.15
 */
 
/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

public class CMDUnban extends CommandAbstract {
    @Override
    public boolean safe(GuildMessageReceivedEvent event,
                        Guild guild,
                        User user){
        try {
            return guild.getMember(user).getPermissions().contains(Permission.BAN_MEMBERS)
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

        //Checks command syntax
        if(args.length != 1){
            event.getChannel().sendMessage("Correct usage: `" + Redis.getInstance().getGuildPrefix(guild) + "Unban [User ID]`").queue();
            return;
        }

        //Unbans the user
        User userToUnban = event.getJDA().retrieveUserById(args[0]).complete();
        if(userToUnban == null){
            event.getChannel().sendMessage("That user ID does not correspond to an existing user.").queue();
        }else{
            guild.getController().unban(userToUnban).complete();
        }

        if(!Redis.getInstance().getGuildModLogID(guild).equals("000")){

            //Setting up user info for the user getting unbanned.
            String unbannedUsername = userToUnban.getName() + "#" + userToUnban.getDiscriminator();
            String unbannedUserID = userToUnban.getId();

            //Setting up user info for the moderator who sent the message.
            String modUserName = user.getName() + "#" + user.getDiscriminator();
            String modUserID = user.getId();

            Long modLogChannelID = Long.parseLong(Redis.getInstance().getGuildModLogID(guild));
            guild.getTextChannelById(modLogChannelID).sendMessage(
                    "**Unbanned :angel:**\n" +
                            "**The Spared: **" + unbannedUsername + " (ID: " + unbannedUserID + ")\n" +
                            "**Moderator: **" + modUserName + " (ID: " + modUserID + ")\n")
                    .queue();
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

        event.getChannel().sendMessage("This command requires the `Ban Members` permission. " + user.getAsMention()).queue();
    }
}