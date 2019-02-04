package com.ibdiscord.data;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

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
public final class LocalConfig {


    /**
     * AUTHORS
     */
    @Getter private final List<String> botAuthors;

    /**
     * DEVELOPERS
     */
    @Getter private final List<Long> developIDs;

    /**
     * GITHUB
     */
    @Getter private final String githubLink;

    /**
     * TOKEN
     */
    @Getter private final String botToken;

    /**
     * VERSION
     */
    @Getter private final String botVersion;

    /**
     * PREFIX
     */
    @Getter private final String staticPrefix;

    /**
     * DATA_HOST
     */
    @Getter private final String dbIP;

    /**
     * DATA_INDEX
     */
    @Getter private final Long mainDatabaseNum;

    /**
     * DATA_AUTH
     */
    @Getter private final String mainDatabasePassword;

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
