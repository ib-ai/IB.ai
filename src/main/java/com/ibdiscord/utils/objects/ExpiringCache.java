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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ExpiringCache<K, V> {

    private final Map<K, V> internal = new ConcurrentHashMap<>();
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private final long time;
    private final TimeUnit unit;

    /**
     * Creates an expiring cache.
     * @param time The time.
     * @param unit The unit for the time.
     */
    public ExpiringCache(long time, TimeUnit unit) {
        this.time = time;
        this.unit = unit;
    }

    /**
     * Adds a value to the cache.
     * @param key The key.
     * @param value The value.
     */
    public void put(K key, V value) {
        internal.put(key, value);
        executor.schedule(() -> internal.remove(key), time, unit);
    }

    /**
     * Gets a value by key.
     * @param key The key.
     * @return The value.
     */
    public V get(K key) {
        return internal.get(key);
    }

}
