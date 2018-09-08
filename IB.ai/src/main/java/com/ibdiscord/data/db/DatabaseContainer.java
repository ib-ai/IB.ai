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
package com.ibdiscord.data.db;

import com.ibdiscord.main.IBai;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;

import static java.lang.Math.toIntExact;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/**
 * @author pants
 * @since 2018.08.21
 */

public class DatabaseContainer {

    private static DatabaseContainer instance;
    private static StatefulRedisConnection<String, String> connection;
    private static RedisAsyncCommands async;
    private static RedisCommands sync;

    // Empty constructor
    private DatabaseContainer() {}

    public static DatabaseContainer getInstance() {
        if(instance == null) {
            instance = new DatabaseContainer();
            return instance;
        }
        return instance;
    }

    public static void connect() {
        String dbIP = IBai.getConfig().getDbIP();
        int mainDbNum = toIntExact(IBai.getConfig().getMainDatabaseNum());
        String mainDbPassword = IBai.getConfig().getMainDatabasePassword();

        if(connection != null){
            return;
        }

        //Generating a URI to be used to generate connection
        RedisURI.Builder uri = RedisURI.Builder.redis(dbIP)
                .withDatabase(mainDbNum)
                .withPassword(mainDbPassword);

        //Connecting with the built URI object
        RedisClient client = RedisClient.create(uri.build());
        connection = client.connect(); //Establishing the connection

        sync = connection.sync();
        async = connection.async();

        System.out.println(sync.get("test"));
    }
}
