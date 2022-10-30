/* Copyright 2018-2020 Jarred Vardy <vardy@riseup.net>, Arraying
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
     * LINK SHORTENER URL.
     */
    @Getter private final String shortUrl;

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
     * SUBJECTS.
     */
    @Getter private final LocalSubjects subjects;

    /**
     * SENSITIVE_ROLES.
     */
    @Getter private final List<Long> sensitiveRoles;

    /**
     * LANGUAGE_BASE.
     */
    @Getter private final String langBase;

    /**
     * OPT_CATEGORIES.
     */
    @Getter private final List<Long> optCategories;

    /**
     * OPT_BLACKLIST.
     */
    @Getter private final List<Long> optBlacklist;

    /**
     * OPT_CHANNEL
     */
    @Getter private final Long optChannel;

    /**
     * NSA_DENYLIST.
     */
    @Getter private final List<Long> nsaDenyList;

    /**
     * Constructor for the local configuration object.
     * Sets all of the class properties to their corresponding environment
     * variable.
     */
    public LocalConfig() {
        final Function<String, List<Long>> lambdaMapId = data -> { // Final to comply with checkstyle.
            if(!(data == null || data.isEmpty())) {
                return Arrays.stream(data.split(";")).map(Long::valueOf).collect(Collectors.toList());
            }
            return new ArrayList<>();
        };

        this.botAuthors = getEnvironment("AUTHORS", raw -> Arrays.asList(raw.split(";")), new ArrayList<>());
        this.developIDs = getEnvironment("DEVELOPERS", raw -> Arrays.stream(raw.split(";"))
                        .map(Long::valueOf)
                        .collect(Collectors.toList()),
                new ArrayList<>());
        this.githubLink = getEnvironment("GITHUB", "https://github.com");
        this.botToken = getEnvironment("TOKEN", "");
        this.botVersion = getEnvironment("VERSION", "BETA");
        this.staticPrefix = getEnvironment("PREFIX", "&");
        this.shortUrl = getEnvironment("LINK_SHORTENER_URL", "");
        this.dbIP = getEnvironment("DATA_HOST", "localhost");
        this.mainDatabaseNum = getEnvironment("DATA_INDEX", Long::valueOf, 0L);
        this.mainDatabasePassword = getEnvironment("DATA_AUTH", null);
        this.subjects = getEnvironment("SUBJECTS", LocalSubjects::new, new LocalSubjects());
        this.langBase = getEnvironment("LANGUAGE_BASE", "/IB.ai/lang/");
        this.sensitiveRoles = getEnvironment("SENSITIVE_ROLES", lambdaMapId, new ArrayList<>());
        this.optCategories = getEnvironment("OPT_CATEGORIES", lambdaMapId, new ArrayList<>());
        this.optBlacklist = getEnvironment("OPT_BLACKLIST", lambdaMapId, new ArrayList<>());
        this.nsaDenyList = getEnvironment("NSA_DENYLIST", lambdaMapId, new ArrayList<>());
        this.optChannel = getEnvironment("OPT_CHANNEL", Long::valueOf, 0L);
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
