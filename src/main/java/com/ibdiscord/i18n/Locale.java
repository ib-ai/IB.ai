/* Copyright 2017-2020 Arraying
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

package com.ibdiscord.i18n;

import com.ibdiscord.utils.UJSON;
import com.ibdiscord.utils.objects.ExpiringCache;
import de.arraying.kotys.JSON;
import de.arraying.kotys.JSONArray;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public final class Locale {

    // 30 minutes is a reasonable amount of time.
    // The bot should not constantly be reading from disk, and storing all values indefinitely is inefficient.
    // For language changes, a full restart is necessary anyway.
    private final ExpiringCache<String, String> cache = new ExpiringCache<>(30, TimeUnit.MINUTES);
    private final File root;
    @Getter private final String name;
    @Getter private final String code;
    @Getter private final String flag;
    private final String encoding;

    /**
     * Attempts to look up a string.
     * This will prioritize the cache, but also try from file.
     * @param key The key, in "filename.key" format.
     * @return The string, or null if it does not exist for this language.
     * @throws LocaleException If the key is invalid.
     */
    String lookup(String key) {
        String[] keyParts = key.split("\\.");
        if(keyParts.length <= 1) {
            throw new LocaleException("invalid key format: please provide filename.key as a language key.");
        }
        String value = cache.get(key);
        if(value == null) {
            value = loadAndGet(key, keyParts); // Passing in keyParts because splitting at runtime is expensive.
        }
        return value; // We don't care if the value is null here, the localisation handler can handle it.
    }

    /**
     * Attempts to load a string and then return it.
     * If it does not exist, null will be returned, and nothing will be cached.
     * @param key The key.
     * @param keyParts The key, but split on ".".
     * @return A string, or null if it does not exist.
     */
    private String loadAndGet(String key, String[] keyParts) {
        String keyFile = keyParts[0];
        String keyJSON = keyParts[1];
        File file = new File(root, keyFile + ".json");
        if(!file.exists()) {
            return null;
        }
        try {
            JSON json = UJSON.retrieveJSONFromFile(file.getPath(), encoding);
            Object valueObject = json.object(keyJSON);
            if(valueObject == null) {
                return null; // Avoid caching null.
            }
            String value;
            if(valueObject instanceof JSONArray) {
                value = ((JSONArray) valueObject).raw().stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(","));
            } else {
                value = valueObject.toString();
            }
            cache.put(key, value);
            return value;
        } catch(IOException exception) {
            throw new LocaleException(exception); // Pass into locale exception.
        }
    }

}
