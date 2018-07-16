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
import gg.discord.ibo.configuration.Configuration;
import gg.discord.ibo.redis.Redis;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

/**
 * @author pants
 * @since 2018.02.17
 */
 
/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

public class CMDTag extends CommandAbstract {
    @Override
    public boolean safe(GuildMessageReceivedEvent event,
                        Guild guild,
                        User user){

        try{
            return (guild.getMember(user).getPermissions().contains(Permission.VIEW_AUDIT_LOGS))
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

        if(args.length < 2 && !args[0].equals("list")){
            event.getChannel().sendMessage("Correct usage: `" + Redis.getInstance().getGuildPrefix(guild) + "tag [list/create/delete] \"[trigger]\" \"[output]\"`").queue();
            return;
        }

        if(args[0].equals("create") || args[0].equals("delete")){
            if(!guild.getMember(user).getPermissions().contains(Permission.MANAGE_SERVER)){
                event.getChannel().sendMessage("This command requires the `Manage Server` permission. " + user.getAsMention()).queue();
                return;
            }
        }

        if(args[0].equals("list")){
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(Color.white);

            StringBuilder stringBuilder = new StringBuilder();

            Map<String, String> allTags = Redis.getInstance().getTags(guild);

            if(allTags.isEmpty()){
                event.getChannel().sendMessage("This guild doesn't have any tags!").queue();
                return;
            }

            for (Map.Entry<String, String> entry : allTags.entrySet()){
                stringBuilder.append("`" + entry.getKey() + "`\n");
                stringBuilder.append(entry.getValue() + "\n");
            }
            stringBuilder.append(" \n`Usage: " + Redis.getInstance().getGuildPrefix(guild) + "tag [list/create/delete] \"[trigger]\" \"[output]\"`");
            embedBuilder.addField("List of Tags:", stringBuilder.toString(), false);
            event.getChannel().sendMessage(embedBuilder.build()).queue();
            return;
        }

        String trigger = null;
        String output = null;
        StringBuilder triggerAndOutputBuilder = new StringBuilder();
        for(int i = 1; i < args.length; i++){
            if(i+1 == args.length){
                triggerAndOutputBuilder.append(args[i]);
                break;
            }

            triggerAndOutputBuilder.append(args[i]);
            triggerAndOutputBuilder.append(" ");
        }


        String triggerAndOutput = triggerAndOutputBuilder.toString();
        Pattern inQuotes = Pattern.compile("(?:^|)\"([^\"]*?)\"(?:$|)");
        Matcher matcher = inQuotes.matcher(triggerAndOutput);
        boolean found = false;
        int groupNum = 0;
        while(matcher.find()){
            if(groupNum == 0){
                trigger = matcher.group(1).replaceAll("\"", "");
            }else{
                output = matcher.group(1).replaceAll("\"", "");
            }
            groupNum++;
            found = true;

        }

        if(!found){
            event.getChannel().sendMessage("Correct usage: `" + Redis.getInstance().getGuildPrefix(guild) + "tag [list/create/delete] \"[trigger]\" \"[output]\"`").queue();
            return;
        }

        if((args[0].equals("create")) && (groupNum == 2)){
            if(!Redis.getInstance().createTag(guild, trigger, output)){
                event.getChannel().sendMessage("That trigger already exists (" + trigger + "). Please delete that tag before creating a new one under this trigger.").queue();
            }
        }else if((args[0].equals("delete")) && (groupNum == 1)){
            Redis.getInstance().deleteTag(guild, trigger);

        }else{
            event.getChannel().sendMessage("Correct usage: `" + Redis.getInstance().getGuildPrefix(guild) + "tag [list/create/delete] \"[trigger]\" \"[output]\"`").queue();
            return;
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

        event.getChannel().sendMessage("The permissions MANAGE_ROLES is required to run that command! " + user.getAsMention()).queue();
    }
}