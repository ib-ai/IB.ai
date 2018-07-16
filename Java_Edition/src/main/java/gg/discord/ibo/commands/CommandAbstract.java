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
package gg.discord.ibo.commands;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

/**
 * @author pants
 * @since 2018.01.02
 */

/* Notes:
 *  Superclass for all commands.
 */

/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

public abstract class CommandAbstract{

    public abstract boolean safe(GuildMessageReceivedEvent event,
                              Guild guild,
                              User user);

    public abstract void execute(GuildMessageReceivedEvent event,
                               Guild guild,
                               User user,
                               Message message,
                               String[] args,
                               String raw);

    public abstract void post(GuildMessageReceivedEvent event,
                              Guild guild,
                              User user,
                              Message message,
                              String[] args,
                              String raw);

    public abstract void onPermissionFailure(GuildMessageReceivedEvent event,
                                             Guild guild,
                                             User user,
                                             Message message,
                                             String[] args,
                                             String raw);
}