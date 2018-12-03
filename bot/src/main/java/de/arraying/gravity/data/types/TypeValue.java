package de.arraying.gravity.data.types;

import de.arraying.gravity.GravityProvider;
import de.arraying.gravity.data.DataType;
import de.arraying.gravity.data.property.Property;

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
public abstract class TypeValue extends DataType {

    private Property property = new Property();

    /**
     * Loads the value.
     * @param provider The provider.
     */
    @Override
    public final void load(GravityProvider provider) {
        property = new Property(provider.get(getUniqueIdentifier()));
    }

    /**
     * Saves the value.
     * @param provider The provider.
     */
    @Override
    public final void save(GravityProvider provider) {
        if(deleted.get()) {
            provider.del(getUniqueIdentifier());
        } else {
            provider.set(getUniqueIdentifier(), property.asString());
        }
    }

    /**
     * Gets the underlying value.
     * @return The underlying value.
     */
    public final Property get() {
        return property;
    }

    /**
     * Sets the underlying value.
     * @param value The new value.
     * @return The new value.
     */
    public final Property set(Object value) {
        if(value == null) {
            throw new IllegalArgumentException("value is null");
        }
        return property = new Property(value.toString());
    }

}
