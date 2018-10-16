/*******************************************************************************
 * Copyright 2018 Jarred Vardy
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
import com.ibdiscord.data.db.DContainer;
import com.ibdiscord.startup.Startup;
import com.ibdiscord.utils.UJavaVersion;
import com.ibdiscord.utils.USplasher;
import com.ibdiscord.utils.exceptions.JavaVersionException;

import lombok.Getter;
import lombok.Setter;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/** @author vardy
 * @since 2018.08.18
 */

public enum IBai {

    /**
     *  Singleton instance of Bot.
     */
    INSTANCE;

    @Getter private static LocalConfig config;
    @Getter @Setter private static DContainer database;

    public static void main(String[] args) throws JavaVersionException {

        /* Checks Java version
         * Error thrown on version != 10 and terminates
         * Docker will handle the JRE10 dependency if executed properly
         */
        UJavaVersion.checkVersion();

        Thread.currentThread().setName("Main");
        IBai.INSTANCE.init();
    }

    private void init() {

        //TODO: Start logging
        config = new LocalConfig();
        Startup.start();
        USplasher.makeASplash();
    }
}
