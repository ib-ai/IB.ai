package de.arraying.gravity.data.types;

import de.arraying.gravity.GravityProvider;
import de.arraying.gravity.data.DataType;
import de.arraying.gravity.data.property.Property;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
@SuppressWarnings("unused")
public abstract class TypeList extends DataType {

    private final List<Property> properties = new ArrayList<>();

    /**
     * Loads the data.
     * @param provider The provider.
     */
    @Override
    public final void load(GravityProvider provider) {
        if(provider == null) {
            throw new IllegalArgumentException("provider is null");
        }
        List<String> values = provider.lrange(getUniqueIdentifier(), 0, -1);
        if(values == null) {
            throw new IllegalStateException("provider returned null list");
        }
        values.stream()
                .map(Property::new)
                .forEach(properties::add);
    }

    /**
     * Saves the data.
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
        properties.forEach(property -> provider.ladd(getUniqueIdentifier(), property.asString()));
    }

    /**
     * Adds a property.
     * @param value The value.
     * @return The newly added property.
     */
    public final Property add(Object value) {
        if(value == null) {
            throw new IllegalArgumentException("value is null");
        }
        Property property = new Property(value.toString());
        properties.add(property);
        return property;
    }

    /**
     * Removes a property.
     * This will remove the first and only the first property with the value.
     * @param value The value.
     * @return The property, or null if it was not present.
     */
    public final Property remove(Object value) {
        if(value == null) {
            throw new IllegalArgumentException("value is null");
        }
        String data = value.toString();
        Property removed = properties.stream()
                .filter(property -> property.asString().equals(data))
                .findFirst()
                .orElse(null);
        if(removed != null) {
            properties.remove(removed);
        }
        return removed;
    }

    /**
     * Gets the contents of the list.
     * @return An immutable list of properties.
     */
    public final List<Property> contents() {
        return Collections.unmodifiableList(properties);
    }

}
