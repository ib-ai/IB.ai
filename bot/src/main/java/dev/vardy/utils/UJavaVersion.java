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

package dev.vardy.utils;

import dev.vardy.LoyalBot;
import dev.vardy.exceptions.JavaVersionException;

public final class UJavaVersion {

    private static final String errorMessage = "You need Java 11 to run this app. Your version: ";

    /**
     * Checks the Java version.
     * @throws JavaVersionException If the version is below the requirement.
     */
    public static void checkVersion() throws JavaVersionException {
        double version = Double.parseDouble(System.getProperty("java.specification.version"));
        LoyalBot.INSTANCE.getLogger().info("Gathered version as \"{}\".", version);
        if(version != 10) {
            throwError(version);
            shutdown();
        }
    }

    /**
     * Shuts down the process.
     */
    private static void shutdown() {
        System.exit(1);
    }

    /**
     * Throws an error.
     * @param version The version.
     * @throws JavaVersionException An exception.
     */
    private static void throwError(double version) throws JavaVersionException {
        throw new JavaVersionException(errorMessage + version);
    }

}
