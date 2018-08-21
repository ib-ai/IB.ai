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
package com.ibdiscord.main;

import com.ibdiscord.data.LocalConfig;
import com.ibdiscord.listeners.MessageListener;
import com.ibdiscord.utils.JavaVersionUtil;
import com.ibdiscord.utils.exceptions.JavaVersionException;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/** @author pants
 * @since 2018.08.18
 */

public enum IBai {

    /**
     *  Singleton instance of Bot.
     */
    INSTANCE;

    private static JDA jda;

    public static void main(String[] args) throws JavaVersionException {

        // Checks Java version
        // Error thrown on version != 10 and exits
        JavaVersionUtil.checkVersion();

        Thread.currentThread().setName("Main");
        IBai.INSTANCE.init();
    }

    private void init() {

        try {
            jda = new JDABuilder(AccountType.BOT)
                    .setToken(LocalConfig.getBotToken())
                    .setStatus(OnlineStatus.DO_NOT_DISTURB)
                    .setGame(Game.playing("a game"))
                    .addEventListener(new MessageListener())
                    .build();
            jda.setAutoReconnect(true);
            jda.awaitReady();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Start logging

        // Get local config
        // Connect to database
        // Start bot

        // Throw up splash screen if all succeeds
    }
}
