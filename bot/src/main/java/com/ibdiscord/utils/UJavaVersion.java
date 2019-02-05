package com.ibdiscord.utils;

import com.ibdiscord.utils.exceptions.JavaVersionException;

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
public final class UJavaVersion {

    private static final String errorMessage = "You need Java 10 or higher to run this app. Your version: ";

    /**
     * Checks the Java version.
     * @throws JavaVersionException If the version is below the requirement.
     */
    public static void checkVersion() throws JavaVersionException {
        double version = Double.parseDouble(System.getProperty("java.specification.version"));

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
