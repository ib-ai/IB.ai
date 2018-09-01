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

import com.ibdiscord.data.LocalConfig;
import com.ibdiscord.data.db.entities.BotMeta;
import com.ibdiscord.data.db.entities.Harry;
import com.ibdiscord.main.IBai;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.util.ArrayList;
import java.util.List;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/**
 * @author pants
 * @since 2018.08.21
 */

public class DatabaseContainer {

    private static Datastore database;

    // Empty constructor
    public DatabaseContainer() {}

    public static void connect() {
        String mainDb = IBai.getConfig().getMainDatabaseName();
        String mainDbUsername = IBai.getConfig().getMainDatabaseUsername();
        String mainDbPassword = IBai.getConfig().getMainDatabasePassword();

        // Instantiating Morphia
        Morphia morphia = new Morphia();

        ServerAddress address = new ServerAddress("localhost", 27017);
        List<MongoCredential> credentialsList = new ArrayList<>();
        MongoCredential credential = MongoCredential.createCredential(
                mainDbUsername, mainDb, mainDbPassword.toCharArray());
        credentialsList.add(credential);

        MongoClient client = new MongoClient(address, credentialsList);

        // setting the main database
        database = morphia.createDatastore(client, mainDb);

        // Declaring map for Morphia to find entity classes
        morphia.mapPackage("com.ibdiscord.data.db.entities");

        // Tells the Morphia mapper to store null and empty values in Mongo
        morphia.getMapper().getOptions().setStoreNulls(true);
        morphia.getMapper().getOptions().setStoreEmpties(true);

        Harry harry = new Harry();
        harry.setName("hazza");
        database.save(harry);

        BotMeta meta = BotMeta.getBotMeta();
        meta.setBotGame("Yeet");
        database.save(meta);

    }
}
