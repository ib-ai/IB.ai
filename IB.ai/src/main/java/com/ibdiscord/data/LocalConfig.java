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
package com.ibdiscord.data;

import com.moandjiezana.toml.Toml;

import lombok.Getter;

import java.io.File;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/** @author pants
 * @since 2018.08.19
 */

public class LocalConfig {
    private static Toml toml;
    private static String localPath = "Config.toml";

    // Metadata
    @Getter private static String[] botAuthors;
    @Getter private static String[] developIDs;
    @Getter private static String githubLink;

    // Bot
    @Getter private static String botToken;
    @Getter private static String botTokenBeta;
    @Getter private static boolean betaMode;
    @Getter private static String botVersion;
    @Getter private static String staticPrefix;

    // Database
    @Getter private static String mongoPort;
    @Getter private static String mongoPassword;


    public static void read() {

        toml = new Toml().read(new File(localPath));

    }
}
