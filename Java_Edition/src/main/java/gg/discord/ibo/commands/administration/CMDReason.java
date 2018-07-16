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
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

/**
 * @author pants
 * @since 2018.02.14
 */
 
/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

public class CMDReason extends CommandAbstract {
    @Override
    public boolean safe(GuildMessageReceivedEvent event,
                        Guild guild,
                        User user){

        try{
            return (guild.getMember(user).getPermissions().contains(Permission.BAN_MEMBERS))
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
        if((!args[0].matches("^[0-9]+$")) || (args.length < 2)){
            event.getChannel().sendMessage("Correct usage: `" + Redis.getInstance().getGuildPrefix(guild) + "Reason [Case Number] [Reason]`").queue();
            return;
        }

        Set<String> allBansSet = Redis.getInstance().getAllBans(guild);
        Object[] allBansArrayObject = allBansSet.toArray();
        Pattern patternFirstSet = Pattern.compile("^[^;]*(.*)$");

        String idOfCaseMessage = null;

        for(int i = 0; i < allBansArrayObject.length; i++){
            Matcher matcher = patternFirstSet.matcher(allBansArrayObject[i].toString());

            if(matcher.matches()){
                String notTheGroupNumber = matcher.group(1);
                String justTheGroupNumber = allBansArrayObject[i].toString().replace(notTheGroupNumber, "");

                if(justTheGroupNumber.equals(args[0])){
                    idOfCaseMessage = allBansArrayObject[i].toString().substring(allBansArrayObject[i].toString().lastIndexOf("?") + 1);
                    break;
                }
            }
        }

        if(idOfCaseMessage == null){
            return;
        }

        StringBuilder reasonBuilder = new StringBuilder();
        for(int i = 1; i < args.length; i++){
            if(i+1 == args.length){
                reasonBuilder.append(args[i]);
                break;
            }

            reasonBuilder.append(args[i]);
            reasonBuilder.append(" ");
        }
        String reason = reasonBuilder.toString();

        String modLogChannelID = Redis.getInstance().getGuildModLogID(guild);

        ///
        guild.getTextChannelById(modLogChannelID).getMessageById(idOfCaseMessage).queue(messageObj ->
            replaceTextWithReason(messageObj.getRawContent(), reason, modLogChannelID, guild, messageObj.getId())
        );
    }

    /**<p>
     * Method used in lambda expression to change the text taken from a Message object.<br>
     * </p>
     */
    private void replaceTextWithReason(String oldMessageContent,
                                       String reason,
                                       String modLogChannelID,
                                       Guild guild,
                                       String oldMessageID){

        String contentToReplace = oldMessageContent.substring(oldMessageContent.lastIndexOf("Reason:"), oldMessageContent.length());
        String newContent = oldMessageContent.replace(contentToReplace, "Reason: **" + reason);

        guild.getTextChannelById(modLogChannelID)
                .editMessageById(oldMessageID, newContent)
                .queue();
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