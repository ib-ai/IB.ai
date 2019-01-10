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
package com.ibdiscord.data;

import com.moandjiezana.toml.Toml;
import lombok.Getter;

import java.io.File;
import java.util.List;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/** @author vardy
 * @since 2018.08.19
 */

public final class LocalConfig {

    /**
     * The local configuration path.
     */
    private static final String LOCAL_PATH = "Config.toml";

    private static Toml config;

    // Metadata
    @Getter private String botName;
    @Getter private List<String> botAuthors;
    @Getter private List<Long> developIDs;
    @Getter private String githubLink;

    // Bot
    @Getter private String botToken;
    @Getter private String botTokenBeta;
    @Getter private boolean betaMode;
    @Getter private String botVersion;
    @Getter private String staticPrefix;

    @Getter private String dbIP;
    @Getter private Long mainDatabaseNum;
    @Getter private String mainDatabasePassword;

    public LocalConfig() {
        config = new Toml().read(new File(LOCAL_PATH));
        this.init();
    }

    /**
     * Initializes the config.
     * TODO: Add CLI
     */
    private void init() {
        // Initialising values
        botName = config.getString("bot_name");

        // [metadata]
        //TODO: Fix list inits
        botAuthors = config.getList("metadata.bot_authors");
        developIDs = config.getList("metadata.developer_ids");
        githubLink = config.getString("metadata.github_link");

        // [bot]
        botToken = config.getString("bot.bot_token");
        botTokenBeta = config.getString("bot.bot_token_beta");
        betaMode = config.getBoolean("bot.beta_mode");
        botVersion = config.getString("bot.bot_version");
        staticPrefix = config.getString("bot.static_prefix");

        // [database]
        dbIP = config.getString("database.db_ip");
        mainDatabaseNum = config.getLong("database.main_db");
        mainDatabasePassword = config.getString("database.main_password");
    }

}
