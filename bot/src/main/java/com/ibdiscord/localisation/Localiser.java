/* Copyright 2019 Jarred Vardy <jarredvardy@gmail.com>
 *
 * This file is part of CORAL.
 *
 * CORAL is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CORAL is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CORAL. If not, see http://www.gnu.org/licenses/.
 */

package com.ibdiscord.localisation;

import com.ibdiscord.exceptions.LocalisationException;
import com.ibdiscord.exceptions.LocaliserSyntaxException;

import net.dv8tion.jda.core.entities.User;

public enum Localiser {

    /**
     *  Singleton instance of Localiser.
     */
    INSTANCE;

    /**
     * Localiser. Used to find the correct localisation of a piece of text based off of
     * the inputted user's language preference.
     *
     */
    public static String __(User user, String key) throws LocaliserSyntaxException {

        String[] splitKey = key.split(".");

        if(splitKey.length != 2) {
            throwSyntaxError(Integer.toString(splitKey.length));
        }

        // Add Redis schema for users' language preferences. Generated (defaulted) or found  when user types any command.
        // Set user language with command:
        // &lang <language>

        // Enter directory based off of user's language preference (stored in Redis, default as English)
        // Find JSON file from category (splitKey[0])
        // Parse JSON and find key (splitKey[1]) in JSON as key
        // {
        //    "key":"value",
        //    "key2":"value2"
        // }
        // Return value based on key.
        // If category (JSON file) is not found, throw LocalisationException (__ parameter 'key' as parameter of throw)
        // If key is not found, throw LocalisationException (__ parameter 'key' as parameter of throw)

        // Add localised command names to command instantiation
        // Add Redis schemas for translation
        // Add startup task that caches all english translations to Redis

        String output = "";
        return output;
    }

    /**
     * Throws a syntax error.
     * @param numOfKeys The number of keys passed to the localiser method.
     * @throws LocaliserSyntaxException Syntax exception.
     */
    private static void throwSyntaxError(String numOfKeys) throws LocaliserSyntaxException {
        throw new LocaliserSyntaxException(numOfKeys);
    }

    /**
     * Throws a localisation error.
     * @param message Message to throw.
     * @throws LocalisationException Localisation exception.
     */
    private static void throwLocalisationError(String message) throws LocalisationException {
        throw new LocalisationException(message);
    }
}
