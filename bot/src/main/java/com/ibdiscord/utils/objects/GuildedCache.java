package com.ibdiscord.utils.objects;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Copyright 2019 Arraying
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
public class GuildedCache<K, V> {

    private final Map<Long, Map<K, V>> internal = new ConcurrentHashMap<>();

    /**
     * Retrieves a value from the cache. If the value is not
     * there, the fallback value will be returned AND inserted.
     * @param guild The guild ID.
     * @param key The key.
     * @param fallback The fallback value.
     * @return Not null, unless fallback was once specified to be as null.
     */
    public V compute(long guild, K key, V fallback) {
        return ofGuild(guild).computeIfAbsent(key, k -> fallback);
    }

    /**
     * Puts a key and a value in the cache.
     * If there is a previous value associated with it, it will be overwritten.
     * @param guild The guild ID.
     * @param key The key.
     * @param value The value.
     */
    public void put(long guild, K key, V value) {
        ofGuild(guild).put(key, value);
    }

    /**
     * Gets the cache of a specific guild.
     * This will create a new map if needed.
     * @param guild The guild ID.
     * @return A map.
     */
    private Map<K, V> ofGuild(long guild) {
        return internal.computeIfAbsent(guild, k -> new HashMap<>());
    }

}
