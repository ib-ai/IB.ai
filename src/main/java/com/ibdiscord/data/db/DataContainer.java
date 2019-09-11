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

package com.ibdiscord.data.db;

import com.ibdiscord.IBai;
import com.ibdiscord.data.LocalConfig;
import de.arraying.gravity.Gravity;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.Getter;

import static java.lang.Math.toIntExact;

public enum DataContainer {

    /**
     * Singleton instance of container.
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

        if(connection != null) {
            return;
        }

        //Connecting with the built URI object
        RedisClient client = RedisClient.create(String.format("redis://%s@%s/%d", mainDbPassword, dbIP, mainDbNum));
        connection = client.connect(); //Establishing the connection

        sync = connection.sync();
        gravity = new Gravity(new DataProvider());
    }

}
