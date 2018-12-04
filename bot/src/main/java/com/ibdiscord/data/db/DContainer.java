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
package com.ibdiscord.data.db;

import com.ibdiscord.main.IBai;

import de.arraying.gravity.Gravity;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.Getter;

import static java.lang.Math.toIntExact;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/**
 * @author vardy
 * @since 2018.08.21
 */

public enum DContainer {

    /** <p> Singleton instance of container.</p>
     */
    INSTANCE;

    @Getter private static StatefulRedisConnection<String, String> connection;
    @Getter private static RedisCommands sync;
    @Getter private static Gravity gravity;

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
        gravity = new Gravity(new DProvider());
    }
}
