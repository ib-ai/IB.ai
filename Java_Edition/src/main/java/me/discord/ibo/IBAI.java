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
package me.discord.ibo;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * */


/**
 * @author pants
 * @since 2018.06.28
 */
 
/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

public class IBAI {

    private static JDA jda;

    public static void main(String[] args) {
        try{
            jda = new JDABuilder(AccountType.BOT)
                    .setToken(readToken())
                    .setStatus(OnlineStatus.DO_NOT_DISTURB)
                    .setGame(Game.of("[.]  DM For Mod-Mail"))
                    .buildBlocking();
            jda.setAutoReconnect(true);

        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private static String readToken() {
        BufferedReader reader = new BufferedReader(new FileReader("token.txt"));
        try {
            return reader.readLine();

        } catch (Exception ex){
            ex.printStackTrace();
        }

        finally {
            reader.close();
        }
    }
}