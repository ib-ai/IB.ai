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

import gg.discord.ibo.IBBot;
import gg.discord.ibo.commands.CommandAbstract;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

/**
 * @author pants
 * @since 2018.02.14
 */
 
/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

public class CMDPing extends CommandAbstract {
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

        double pingMilli = (double) IBBot.getJDA().getPing() / 1000;
        String pong = String.format("%.2f", pingMilli);

        String pingpong = "Pong! \n IB-Bot responded in `" + pong + "` seconds!";
        event.getChannel().sendMessage(pingpong).queue();
    }

    @Override
    public void post(GuildMessageReceivedEvent event, Guild guild, User user, Message message, String[] args, String raw) {
        ///
    }

    @Override
    public void onPermissionFailure(GuildMessageReceivedEvent event,
                                    Guild guild,
                                    User user,
                                    Message message,
                                    String[] args,
                                    String raw) {
        ///
    }
}