package com.ibdiscord.data.db;

import com.ibdiscord.IBai;
import com.ibdiscord.data.LocalConfig;
import de.arraying.gravity.Gravity;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.Getter;

import static java.lang.Math.toIntExact;

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
public enum DContainer {

    /**
     * <p> Singleton instance of container.</p>
     */
    INSTANCE;

    @Getter private StatefulRedisConnection<String, String> connection;
    @Getter private RedisCommands sync;
    @Getter private Gravity gravity;

    /**
     * Connect to the database.
     */
    public void connect() {
        LocalConfig config = IBai.INSTANCE.getConfig();
        String dbIP = config.getDbIP();
        int mainDbNum = toIntExact(config.getMainDatabaseNum());
        String mainDbPassword = config.getMainDatabasePassword();

        if(connection != null){
            return;
        }

        //Connecting with the built URI object
        RedisClient client = RedisClient.create(String.format("redis://%s@%s/%d", mainDbPassword, dbIP, mainDbNum));
        connection = client.connect(); //Establishing the connection

        sync = connection.sync();
        gravity = new Gravity(new DProvider());
    }

}
