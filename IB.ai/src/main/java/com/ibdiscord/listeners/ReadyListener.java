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
package com.ibdiscord.listeners;

import net.dv8tion.jda.bot.entities.ApplicationInfo;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/**
 * @author pants
 * @since 2018.09.05
 */

public class ReadyListener extends ListenerAdapter {

    @Override
    public void onReady(ReadyEvent event) {

        ApplicationInfo appInfo = event.getJDA().asBot().getApplicationInfo().complete();
        String botName = appInfo.getName();
        String botOwner = appInfo.getOwner().getName();
        String botDescription = appInfo.getDescription();
        boolean isPublicBot = appInfo.isBotPublic();
        int guildNum = event.getJDA().getGuilds().size();

        System.out.println("|| Bot \"" + botName + "\" by "+ botOwner +" is now connected.");
        System.out.println("|| Available to : " + guildNum + " guilds.");
        System.out.println("|| Public bot?  : " + isPublicBot);
        System.out.println("|| Description  : " + botDescription);
    }
}
