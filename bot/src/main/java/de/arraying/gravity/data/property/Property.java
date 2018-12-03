package de.arraying.gravity.data.property;

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
public final class Property {

    private final String value;

    /**
     * Creates a new property.
     */
    public Property() {
        this(null);
    }

    /**
     * Creates a new property.
     * @param value The value of the property.
     */
    public Property(String value) {
        this.value = value;
    }

    /**
     * Defines that the property should default to something.
     * @param fallback The default value.
     * @return The property that will definitely contain a value.
     */
    public Property defaulting(Object fallback) {
        return value != null ? this : new Property(fallback.toString());
    }

    /**
     * Gets the value as a boolean.
     * @return A boolean.
     */
    public boolean asBoolean() {
        return Boolean.valueOf(value);
    }

    /**
     * Gets the value as a byte.
     * @return A byte.
     */
    public byte asByte() {
        return Byte.valueOf(value);
    }

    /**
     * Gets the value as a character.
     * @return A character.
     */
    public char asChar() {
        return value.length() == 0 ? 0 : value.charAt(0);
    }

    /**
     * Gets the value as a short.
     * @return A short.
     */
    public short asShort() {
        return Short.valueOf(value);
    }

    /**
     * Gets the value as an integer.
     * @return An integer.
     */
    public int asInt() {
        return Integer.valueOf(value);
    }

    /**
     * Gets the value as a long.
     * @return A long.
     */
    public long asLong() {
        return Long.valueOf(value);
    }

    /**
     * Gets the value as a float decimal.
     * @return A float.
     */
    public float asFloat() {
        return Float.valueOf(value);
    }

    /**
     * Gets the value as a double decimal.
     * @return A double.
     */
    public double asDouble() {
        return Double.valueOf(value);
    }

    /**
     * Gets the value as a string.
     * @return A string.
     */
    public String asString() {
        return value;
    }

    /**
     * Gets the value as a custom type.
     * @param converter The type converter instance to use.
     * @param <T> The custom type.
     * @return A custom type value.
     */
    public <T> T as(PropertyConverter<T> converter) {
        return converter.convert(value);
    }

    /**
     * Gets the object as a string.
     * @return The string representation.
     */
    @Override
    public String toString() {
        return asString();
    }

}
