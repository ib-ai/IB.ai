package de.arraying.gravity.data.types;

import de.arraying.gravity.GravityProvider;
import de.arraying.gravity.data.DataType;
import de.arraying.gravity.data.property.Property;

import java.util.Collections;
import java.util.HashSet;
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
@SuppressWarnings({"unused", "UnusedReturnValue"})
public abstract class TypeSet extends DataType {

    private final Set<Property> properties = new HashSet<>();

    /**
     * Loads the set.
     * @param provider The provider.
     */
    @Override
    public final void load(GravityProvider provider) {
        if(provider == null) {
            throw new IllegalArgumentException("provider is null");
        }
        Set<String> values = provider.smembers(getUniqueIdentifier());
        if(values == null) {
            throw new IllegalStateException("provider returned null for members");
        }
        properties.clear();
        values.stream()
                .map(Property::new)
                .forEach(properties::add);
    }

    /**
     * Saves the set.
     * @param provider The provider.
     */
    @Override
    public final void save(GravityProvider provider) {
        if(provider == null) {
            throw new IllegalArgumentException("provider is null");
        }
        provider.del(getUniqueIdentifier());
        if(deleted.get()) {
            return;
        }
        properties.forEach(property -> provider.sadd(getUniqueIdentifier(), property.asString()));
    }

    /**
     * Adds a property to the set.
     * @param value The value.
     * @return The added property.
     */
    public final Property add(Object value) {
        if(value == null) {
            throw new IllegalArgumentException("value is null");
        }
        String data = value.toString();
        Property property = get(data);
        if(property == null) {
            property = new Property(data);
            properties.add(property);
        }
        return property;
    }

    /**
     * Removes a property from the set.
     * @param value The value.
     * @return The removed property, or null if it was not present.
     */
    public final Property remove(Object value) {
        if(value == null) {
            throw new IllegalArgumentException("value is null");
        }
        Property property = get(value.toString());
        if(property != null) {
            properties.remove(property);
        }
        return property;
    }

    /**
     * Whether or not a property is in the set.
     * @param value The value.
     * @return True if it is, false otherwise.
     */
    public final boolean contains(Object value) {
        return get(value.toString()) != null;
    }

    /**
     * Gets the set values.
     * @return An unmodifiable set of values.
     */
    public final Set<Property> values() {
        return Collections.unmodifiableSet(properties);
    }

    /**
     * Gets the size of the set.
     * @return The size of the set.
     */
    public final long size() {
        return properties.size();
    }

    /**
     * Gets a property by value.
     * @param value The value.
     * @return The property, or null if it does not exist.
     */
    private Property get(String value) {
        return properties.stream()
                .filter(it -> it.asString().equals(value))
                .findFirst()
                .orElse(null);
    }

}
