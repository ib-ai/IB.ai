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

import gg.discord.ibo.IBBot;
import gg.discord.ibo.configuration.Configuration;

import net.dv8tion.jda.core.entities.Guild;

import java.io.IOException;
import java.util.Objects;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

/**
 * @author pants
 * @since 2018.03.07
 */

/* * * * * * * * * * * * * * * * * * * * * * * * * * * */

public class UtilsCertifier{

    public static boolean hasCertificate(Guild guild){

        try{
            return(guild.getId().equals(Configuration.getInstance().getMainServerID())
                    || (guild.getId().equals("303498127854272522") && Objects.equals(IBBot.getJDA().getToken(), "Bot " + Configuration.getInstance().getBotBetaToken())));

        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }
}
