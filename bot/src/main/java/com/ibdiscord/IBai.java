/* Copyright 2017-2019 Jarred Vardy
 *
 * This file is part of IB.ai.
 *
 * IB.ai is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 *
 * IB.ai is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License 
 * along with IB.ai. If not, see http://www.gnu.org/licenses/.
 */

package com.ibdiscord;

import com.ibdiscord.data.LocalConfig;
import com.ibdiscord.exceptions.JavaVersionException;
import com.ibdiscord.startup.Startup;
import com.ibdiscord.utils.UFormatter;
import com.ibdiscord.utils.UJavaVersion;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum IBai {

    /**
     *  Singleton instance of Bot.
     */
    INSTANCE;

    @Getter private LocalConfig config;
    @Getter Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Entry point of the program.
     * @param args The arguments.
     * @throws JavaVersionException An exception in the Java version.
     */
    public static void main(String[] args) throws JavaVersionException {

        /* Checks Java version
         * Error thrown on version != 10 and terminates
         * Docker will handle the JRE10 dependency if executed properly
         */
        UJavaVersion.checkVersion();

        Thread.currentThread().setName("Main");
        IBai.INSTANCE.init();
    }

    /**
     * Initializes the bot.
     */
    private void init() {
        config = new LocalConfig();
        Startup.start();
        UFormatter.makeASplash();
    }

}
