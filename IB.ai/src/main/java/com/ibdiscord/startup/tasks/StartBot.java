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
package com.ibdiscord.startup.tasks;

import com.ibdiscord.listeners.MessageListener;
import com.ibdiscord.listeners.ReadyListener;
import com.ibdiscord.main.IBai;
import com.ibdiscord.startup.AbstractStartupTask;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/**
 * @author pants
 * @since 2018.08.22
 */

public class StartBot extends AbstractStartupTask {

    // TODO: Move to proper bot instantiater
    private static JDA jda;

    public StartBot() {
        super("Start-Bot");
    }

    @Override
    public void doTask() throws Exception {
        String token = IBai.getConfig().getBotToken();
        String botGame = "3.0 | Limited Beta";

        // TODO: Move to proper bot instantiater
        jda = new JDABuilder(AccountType.BOT)
                .setToken(token)
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .setGame(Game.playing(botGame))
                .addEventListener(new MessageListener())
                .addEventListener(new ReadyListener())
                .build();
        jda.setAutoReconnect(true);
        jda.awaitReady();
    }

    // Dev branch
}
