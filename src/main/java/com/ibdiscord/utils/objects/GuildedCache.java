/* Copyright 2017-2019 Arraying
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

package com.ibdiscord.utils.objects;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
