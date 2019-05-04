/**
 * Copyright 2017-2019 Jarred Vardy
 *
 * This file is part of LoyalBot.
 *
 * LoyalBot is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LoyalBot is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LoyalBot. If not, see http://www.gnu.org/licenses/.
 */

package dev.vardy.data.db;

import dev.vardy.LoyalBot;
import dev.vardy.data.LocalConfig;

import de.arraying.gravity.Gravity;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

import lombok.Getter;

import static java.lang.Math.toIntExact;

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
        LocalConfig config = LoyalBot.INSTANCE.getConfig();
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
