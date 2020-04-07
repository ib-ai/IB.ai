/* Copyright 2018-2020 Jarred Vardy <vardy@riseup.net>
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

package com.ibdiscord.utils;

import com.ibdiscord.IBai;
import com.ibdiscord.exceptions.JavaVersionException;

public final class UJavaVersion {

    private static final String ERROR_MESSAGE = "You need Java 10 to run this app. Your version: ";

    /**
     * Checks the Java version.
     * @throws JavaVersionException If the version is below the requirement.
     */
    public static void checkVersion() throws JavaVersionException {
        double version = Double.parseDouble(System.getProperty("java.specification.version"));
        IBai.INSTANCE.getLogger().info("Gathered version as \"{}\".", version);
        if(version != 10) {
            throwError(version);
            System.exit(1);
        }
    }

    /**
     * Throws an error.
     * @param version The version.
     * @throws JavaVersionException An exception.
     */
    private static void throwError(double version) throws JavaVersionException {
        throw new JavaVersionException(ERROR_MESSAGE + version);
    }

}
