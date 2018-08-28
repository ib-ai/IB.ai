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
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package gg.discord.ibo.utils;

import gg.discord.ibo.IBBot;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.managers.GuildController;

import java.util.List;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/**
 * @author pants
 * @since 2018.08.28
 */

public class UtilMassKicker implements Runnable{

    public static void kickAllNonVerifiedUsers() {
        Guild ibServer = IBBot.getJDA().getGuildById("230296179991248896");
        Role verifiedRole = ibServer.getRolesByName("Verified", true).get(0);
        GuildController serverController = ibServer.getController();
        int kickCount = 0;

        try{
            List<Member> members = ibServer.getMembers();
            for(Member mem : members) {
                try{
                    if(!(mem.getRoles().contains(verifiedRole))) {
                        serverController.kick(mem).complete();
                        kickCount++;
                    }

                }catch(Exception ex) {
                    ex.printStackTrace();
                }
            }

        } catch(Exception ex) {
            ex.printStackTrace();
        }

        System.out.println("Kicked [" + kickCount + "] users.");
    }

    public void create() {
        Thread currentThread = new Thread(this);
        currentThread.setName("kicker thread");
        currentThread.start();
    }

    @Override
    public void run() {
        kickAllNonVerifiedUsers();
    }
}
