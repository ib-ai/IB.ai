package de.arraying.gravity.data;

import de.arraying.gravity.GravityProvider;

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
@SuppressWarnings("unused")
public abstract class DataType {

    protected final AtomicBoolean deleted = new AtomicBoolean(false);

    /**
     * Gets the unique identifier.
     * @return A unique identifier.
     */
    protected abstract String getUniqueIdentifier();

    /**
     * Loads the data.
     * @param provider The provider.
     */
    public abstract void load(GravityProvider provider);

    /**
     * Saves the data.
     * @param provider The provider.
     */
    public abstract void save(GravityProvider provider);

    /**
     * Deletes the data type.
     */
    public final void delete() {
        deleted.set(true);
    }

}
