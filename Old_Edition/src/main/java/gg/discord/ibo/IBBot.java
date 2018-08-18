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
package gg.discord.ibo;

import gg.discord.ibo.commands.Commands;
import gg.discord.ibo.configuration.Configuration;
import gg.discord.ibo.listeners.BotJoinListener;
import gg.discord.ibo.listeners.BotLeaveListener;
import gg.discord.ibo.listeners.JoinListener;
import gg.discord.ibo.listeners.MessageListener;
import gg.discord.ibo.redis.Redis;

import gg.discord.ibo.utils.UtilSubjects;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;

import java.io.IOException;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

/**
 * @author pants
 * @since 2018.01.02
 */

/* Notes:
 *  Class responsible for full initialisation.
 *  Singleton class.
 */

/* * * * * * * * * * * * * * * * * * * * * * * * * * * */


public class IBBot{

    private static JDA jda;
    private static IBBot IBBotInstance;

    private static boolean start;

    private IBBot(){}

    /**<p>
     * Startup script for the bot, going through necessary phases.<br>
     * Ensures the process only occurs once in the bot's lifecycle.</p>
     * @throws IllegalStateException Exception thrown when method runs twice.
     * @see IBBot
     * @see Configuration
     * @see Redis
     */
    static void startup() throws IOException{
        if(start){
            throw new IllegalStateException("Bot has already started");
        }

        Redis.getInstance().setupRedis();
        Configuration.getInstance();
        UtilSubjects.initSubjectList();
        IBBot.getInstance().initBotJDA();
        Commands.getInstance().addCommands();
        Redis.getInstance().initValues(jda);

        start = true;
    }

    /**<p>
     * Mechanism which is used to maintain a single instance of {@link IBBot} any one time.</p>
     * @return {@link IBBot} as a Singleton instance.
     */
    public static IBBot getInstance(){
        if (IBBotInstance == null) {
            IBBotInstance = new IBBot();
        }
        return IBBotInstance;
    }

    /**<p>
     * Starts JDA using JDABuilder - Configures JDABuilder.<br>
     * Bot listeners are instances of the listener classes in gg.discord.ibo.listeners, which extend ListenerAdapter.<br>
     * Bot token is taken from Configuration.JSON - This connects the bot to the host client.<br>
     * The game is displayed below the bot's name in the discord client.</p>
     */
    private void initBotJDA(){
        try{
            jda = new JDABuilder(AccountType.BOT)
                                .setToken(Configuration.getInstance().getBotToken())
                                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                                .setGame(Game.of(Configuration.getInstance().getBotGame()))
                                .addEventListener(new MessageListener())
                                .addEventListener(new JoinListener())
                                .addEventListener(new BotJoinListener())
                                .addEventListener(new BotLeaveListener())
                                .buildBlocking();
            jda.setAutoReconnect(true);

        }catch(Exception ex){

            ex.printStackTrace();
        }
    }

    public static JDA getJDA(){
        return jda;
    }
}