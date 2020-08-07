/* Copyright 2020 Nathaneal Varghese
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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.EnumSet;

public enum DataContainerPostgres {

    /**
     * Singleton instance of container.
     */
    INSTANCE;

    private final String username = "postgres";
    private final String password = "password";

    /**
     * Connect to the database.
     */
    public void connect() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://postgredb/", username, password);
            Statement statement = connection.createStatement();
            String sql = "CREATE DATABASE ibai";
            statement.executeUpdate(sql);
            System.out.println("Created IB-ai");
            statement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        try {
            Connection conn = DriverManager.getConnection("jdbc:postgresql://postgredb/ibai", username, password);
            System.out.println("Connected to the PostgreSQL server successfully.");

            Statement statement = conn.createStatement();

            EnumSet.allOf(SQLQuery.class).forEach(query -> {
                if (query.name().startsWith("CREATE_")) {
                    try {
                        statement.addBatch(query.toString());
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                }
            });

            statement.executeBatch();
            statement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
