/* Copyright 2017-2019 Jarred Vardy, Arraying
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

package com.ibdiscord.data;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class LocalConfig {

    /**
     * AUTHORS.
     */
    @Getter private final List<String> botAuthors;

    /**
     * DEVELOPERS.
     */
    @Getter private final List<Long> developIDs;

    /**
     * GITHUB.
     */
    @Getter private final String githubLink;

    /**
     * TOKEN.
     */
    @Getter private final String botToken;

    /**
     * VERSION.
     */
    @Getter private final String botVersion;

    /**
     * PREFIX.
     */
    @Getter private final String staticPrefix;

    /**
     * DATA_HOST.
     */
    @Getter private final String dbIP;

    /**
     * DATA_INDEX.
     */
    @Getter private final Long mainDatabaseNum;

    /**
     * DATA_AUTH.
     */
    @Getter private final String mainDatabasePassword;

    /**
     * API_BASE.
     */
    @Getter private final String apiBase;

    /**
     * Constructor for the local configuration object.
     * Sets all of the class properties to their corresponding environment
     * variable.
     */
    public LocalConfig() {
        this.botAuthors = getEnvironment("AUTHORS", raw -> Arrays.asList(raw.split(";")), new ArrayList<>());
        this.developIDs = getEnvironment("DEVELOPERS", raw -> Arrays.stream(raw.split(";"))
                    .map(Long::valueOf)
                    .collect(Collectors.toList()),
                new ArrayList<>());
        this.githubLink = getEnvironment("GITHUB", "https://github.com");
        this.botToken = getEnvironment("TOKEN", "");
        this.botVersion = getEnvironment("VERSION", "BETA");
        this.staticPrefix = getEnvironment("PREFIX", "&");
        this.dbIP = getEnvironment("DATA_HOST", "localhost");
        this.mainDatabaseNum = getEnvironment("DATA_INDEX", Long::valueOf, 0L);
        this.mainDatabasePassword = getEnvironment("DATA_AUTH", null);
        this.apiBase = getEnvironment("API_BASE", "http://localhost:80");
    }

    /**
     * Gets an environment variable.
     * @param key The key.
     * @param fallback The fallback value.
     * @return A non null value.
     */
    private String getEnvironment(String key, String fallback) {
        String value = System.getenv(key);
        return value == null ? fallback : value;
    }

    /**
     * Gets an environment variable and maps it to a certain type.
     * @param key The key.
     * @param function The mapping function.
     * @param fallback The fallback value.
     * @param <T> The type.
     * @return A non null value.
     */
    private <T> T getEnvironment(String key, Function<String, T> function, T fallback) {
        String raw = getEnvironment(key, null);
        if(raw == null) {
            return fallback;
        }
        return function.apply(raw);
    }

}
