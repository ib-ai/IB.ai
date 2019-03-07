package com.ibdiscord.utils.objects;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
