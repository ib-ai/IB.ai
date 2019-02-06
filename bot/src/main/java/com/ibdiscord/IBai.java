package com.ibdiscord;

import com.ibdiscord.data.LocalConfig;
import com.ibdiscord.startup.Startup;
import com.ibdiscord.utils.UFormatter;
import com.ibdiscord.utils.UJavaVersion;
import com.ibdiscord.exceptions.JavaVersionException;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Copyright 2019 Jarred Vardy
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
