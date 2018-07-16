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
package gg.discord.ibo.utils;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.managers.GuildController;

import java.util.Objects;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

/**
 * @author pants
 * @since 2018.07.12
 */
 
/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

public class UtilCleanup{
    public static void clean(JDA jda){
        System.out.println("Starting cleanup");

        // Prune before continuing
        // Role ID correctness to be tested
        // Test member online status is actually correct strings

        Guild guild = jda.getGuildById("230296179991248896");
        GuildController guildController = guild.getController();
        Role roleVerified = guild.getRoleById("466944758934405121");
        Role roleMod = guild.getRoleById("230710782038376449");
        Role roleMuted = guild.getRoleById("400249997813350411");

        // Mute online members
        int i = 0;
        int z = 0;
        for(Member mem : guild.getMembers()){ // For each member in the server
            try {
                if(Objects.equals(mem.getOnlineStatus().toString(), "OFFLINE") // If user is not offline or unknown
                        || Objects.equals(mem.getOnlineStatus().toString(), "UNKNOWN")){
                    i++;

                }else{
                    try {
                        guildController.addSingleRoleToMember(mem, roleMuted).queue(); // Mute user
                    } catch (Exception e) {
                        System.out.println("Exception in muting an online member.");
                    }
                    z++;
                }
            } catch (Exception e) {
                System.out.println("Exception in testing guild member's online status.");
            }
        }
        System.out.println("Muted " + z + " users.");

        // Ban mods
        int modBans = 0;
        for (Member mem : guild.getMembersWithRoles(roleMod)){
            try {
                if(!mem.getUser().getId().equals("378130362091569152")){
                    guildController.ban(mem.getUser(), 0).queue();
                }
            } catch (Exception e) {
                System.out.println("Exception in banning a mod.");
            }
            modBans++;
        }
        System.out.println("Muted " + modBans + " users.");

        // Delete channels
        int deletedChannels = 0;
        for (TextChannel channel : guild.getTextChannels()){
            try {
                channel.delete().queue();
            } catch (Exception e) {
                System.out.println("Exception in deleting a text channel");
            }
            deletedChannels++;
        }

        for (VoiceChannel channel : guild.getVoiceChannels()){
            try {
                channel.delete().queue();
            } catch (Exception e) {
                System.out.println("Exception in deleting a voice channel.");
            }
            deletedChannels++;
        }
        System.out.println("Deleted " + deletedChannels + " channels.");

        // Ban all users
        int membersBanned = 0;
        for(Member mem : guild.getMembers()){
            try {
                if(!mem.getUser().getId().equals("378130362091569152")) {
                    guildController.ban(mem.getUser(), 0).queue();
                }
            } catch (Exception e) {
                System.out.println("Exception in banning a user.");
            }
            membersBanned++;
        }
        System.out.println("Banned " + membersBanned + " members.");
        System.out.println(guild.getMembers().size() + " users remain on the server.");
    }
}