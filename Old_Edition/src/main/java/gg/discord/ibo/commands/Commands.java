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

import gg.discord.ibo.commands.administration.*;
import gg.discord.ibo.commands.developer.CMDEval;
import gg.discord.ibo.commands.subjects.*;
import gg.discord.ibo.commands.utilities.*;
import gg.discord.ibo.commands.utilities.core.CMDSetModLog;
import gg.discord.ibo.commands.utilities.core.CMDSetPrefix;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.HashMap;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

/**
 * @author pants
 * @since 2018.01.08
 */
 
/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

public class Commands{
    private static Commands instance;
    private HashMap<String, CommandAbstract> premadeCommands = new HashMap<>();

    private Commands(){}

    public static Commands getInstance(){
        if(instance == null){
            instance = new Commands();
        }
        return instance;
    }

    public void addCommands(){
        premadeCommands.put("help", new CMDHelp());
        premadeCommands.put("ban", new CMDBan());
        premadeCommands.put("ping", new CMDPing());
        premadeCommands.put("reason", new CMDReason());
        premadeCommands.put("unban", new CMDUnban());
        premadeCommands.put("setmodlog", new CMDSetModLog());
        premadeCommands.put("setprefix", new CMDSetPrefix());
        premadeCommands.put("kick", new CMDKick());
        premadeCommands.put("userinfo", new CMDUserInfo());
        premadeCommands.put("serverinfo", new CMDServerInfo());
        premadeCommands.put("tag", new CMDTag());
        premadeCommands.put("roleswap", new CMDRoleSwap());
        premadeCommands.put("addsubject", new CMDAddSubject());
        premadeCommands.put("delsubject", new CMDDelSubject());
        premadeCommands.put("subjectlist", new CMDSubjectList());
        premadeCommands.put("getprefix", new CMDGetPrefix());
        premadeCommands.put("join", new CMDJoin());
        premadeCommands.put("leave", new CMDLeave());
        premadeCommands.put("verify", new CMDVerify());
        premadeCommands.put("purge", new CMDPurge());
        premadeCommands.put("userroles", new CMDUserRoles());
        premadeCommands.put("mute", new CMDMute());
        premadeCommands.put("eval", new CMDEval());
    }

    public static void handleCommand(GuildMessageReceivedEvent event,
                                     Guild guild,
                                     User user,
                                     Message message,
                                     String[] args,
                                     String invoke,
                                     String raw){

        HashMap<String, CommandAbstract> premadeCommands = Commands.getInstance().premadeCommands;

        if(premadeCommands.containsKey(invoke)){
            boolean safe = premadeCommands.get(invoke).safe(event, guild, user);

            if(safe){
                premadeCommands.get(invoke).execute(event, guild, user, message, args, raw);
                premadeCommands.get(invoke).post(event, guild, user, message, args, raw);
            }else{
                premadeCommands.get(invoke).onPermissionFailure(event, guild, user, message, args, raw);
            }
        }
    }

    public HashMap<String, CommandAbstract> getPremadeCommands(){
        return premadeCommands;
    }
}