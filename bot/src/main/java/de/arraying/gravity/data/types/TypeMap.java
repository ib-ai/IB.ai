package de.arraying.gravity.data.types;

import de.arraying.gravity.GravityProvider;
import de.arraying.gravity.data.DataType;
import de.arraying.gravity.data.property.Property;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

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
@SuppressWarnings({"unused", "UnusedReturnValue"})
public abstract class TypeMap extends DataType {

    private final AtomicBoolean removal = new AtomicBoolean(false);
    private final Map<String, Property> properties = new ConcurrentHashMap<>();

    /**
     * Loads the data.
     * @param provider The provider.
     */
    @Override
    public final void load(GravityProvider provider) {
        if(provider == null) {
            throw new IllegalArgumentException("provider null");
        }
        Set<String> keys = provider.hkeys(getUniqueIdentifier());
        if(keys == null) {
            throw new IllegalStateException("provider returned null for keys");
        }
        properties.clear();
        keys.stream()
                .map(String::toLowerCase)
                .forEach(key -> set(key, provider.hget(getUniqueIdentifier(), key)));
    }

    /**
     * Saves the data.
     * @param provider The provider.
     */
    @Override
    public final void save(GravityProvider provider) {
        if(provider == null) {
            throw new IllegalArgumentException("provider null");
        }
        if(deleted.get()) {
            provider.del(getUniqueIdentifier());
            return;
        }
        if(removal.get()) {
            Set<String> keys = provider.hkeys(getUniqueIdentifier());
            keys.stream()
                    .map(String::toLowerCase)
                    .filter(key -> !properties.containsKey(key))
                    .forEach(key -> provider.hdel(getUniqueIdentifier(), key));
        }
        properties.forEach((key, value) -> provider.hset(getUniqueIdentifier(), key, value.asString()));
    }

    /**
     * Gets the property by key.
     * @param key The key.
     * @return Never null property (content may be null).
     */
    public final Property get(String key) {
        if(key == null) {
            throw new IllegalArgumentException("key is null");
        }
        key = key.toLowerCase();
        return properties.getOrDefault(key, new Property());
    }

    /**
     * Gets all the keys in the TypeMap.
     * @return
     */
    public final Set<String> getKeys() {
        return properties.keySet();
    }

    /**
     * Sets the property to a new value.
     * @param key The key.
     * @param value The value.
     * @return The previous property, may be null.
     */
    public final Property set(String key, Object value) {
        if(key == null) {
            throw new IllegalArgumentException("key is null");
        }
        if(value == null) {
            throw new IllegalArgumentException("value is null");
        }
        key = key.toLowerCase();
        return properties.put(key, new Property(value.toString()));
    }

    /**
     * Unsets a property.
     * @param key The key.
     * @return The previous property, may be null.
     */
    public final Property unset(String key) {
        if(key == null) {
            throw new IllegalArgumentException("key is null");
        }
        key = key.toLowerCase();
        Property previous = properties.getOrDefault(key, new Property());
        properties.remove(key);
        removal.set(true);
        return previous;
    }

}