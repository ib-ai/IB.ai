package de.arraying.gravity;

import java.util.List;
import java.util.Set;

/**
 * Copyright 2018 Arraying
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
public interface GravityProvider {

    /**
     * Gets a value.
     * @param key The key.
     * @return The value, or null if it does not exist.
     */
    String get(String key);

    /**
     * Sets a value.
     * @param key The key.
     * @param value The value.
     */
    void set(String key, String value);

    /**
     * Deletes a value.
     * @param key The key.
     */
    void del(String key);

    /**
     * Gets a hash property.
     * @param key The key.
     * @param property The property.
     * @return The value, or null if it does not exist.
     */
    String hget(String key, String property);

    /**
     * Gets all properties for a hash.
     * @param key The key.
     * @return A set, this must be empty if the key does not exist/is invalid.
     */
    Set<String> hkeys(String key);

    /**
     * Sets a hash property.
     * @param key The key.
     * @param property The property.
     * @param value The value.
     */
    void hset(String key, String property, String value);

    /**
     * Deletes a hash property.
     * @param key The key.
     * @param property The property.
     */
    void hdel(String key, String property);

    /**
     * Gets all entries for a set.
     * @param key The key.
     * @return A set, this must be empty if the key does not exist/is invalid.
     */
    Set<String> smembers(String key);

    /**
     * Adds an entry to the set.
     * @param key The key.
     * @param value The value.
     */
    void sadd(String key, String value);

    /**
     * Adds an entry to the list.
     * @param key The key.
     * @param value The value.
     */
    void ladd(String key, String value);

    /**
     * Gets a range of values from the list.
     * @param key The key.
     * @param from The inclusive from value.
     * @param to The exclusive to value.
     * @return A list, this must be empty if the key does not exist/is invalid.
     */
    List<String> lrange(String key, int from, int to);

}
