package de.arraying.gravity;

import de.arraying.gravity.data.DataType;

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
public final class Gravity {

    private final GravityProvider provider;

    /**
     * Creates a new instance of Gravity.
     * This instance can then be used to handle the data.
     * @param provider The provider. Gravity does not interact with
     * Redis itself, therefore it needs this interface to grab raw data.
     */
    public Gravity(GravityProvider provider) {
        this.provider = provider;
    }

    /**
     * Loads the datatype.
     * @param t A non-null datatype instance.
     * @param <T> The datatype type.
     * @return The passed in type, loaded.
     */
    public <T extends DataType> T load(T t) {
        if(t == null) {
            throw new IllegalArgumentException("data type is null");
        }
        t.load(provider);
        return t;
    }

    /**
     * Saves the datatype.
     * @param t A non-null datatype instance.
     * @param <T> The datatype type.
     * @return The passed in type, saved.
     */
    public <T extends DataType> T save(T t) {
        if(t == null) {
            throw new IllegalArgumentException("data type is null");
        }
        t.save(provider);
        return t;
    }

}
