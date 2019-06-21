/**
 * Copyright 2017-2019 Jarred Vardy <jarred.vardy@gmail.com>
 *
 * This file is part of IB.ai.
 *
 * IB.ai is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * IB.ai is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with IB.ai. If not, see http://www.gnu.org/licenses/.
 */

package com.ibdiscord.localisation;

import com.ibdiscord.command.CommandContext;
import com.ibdiscord.data.db.DContainer;
import com.ibdiscord.data.db.entries.LangData;
import com.ibdiscord.exceptions.LocalisationException;
import com.ibdiscord.exceptions.LocaliserSyntaxException;

import de.arraying.gravity.Gravity;
import de.arraying.kotys.JSON;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

public enum Localiser {

    /**
     *  Singleton instance of Localiser.
     */
    INSTANCE;

    /**
     * Localiser. Used to find the correct localisation of a piece of text based off of
     * the inputted user's language preference.
     * @param commandContext The context of the command this method is called from
     * @param key The identifier for the text that is to be found < category.key >
     * @throws LocaliserSyntaxException When the key is syntactically incorrect
     * @return The localised text corresponding to the inputted key
     */
    public static String __(CommandContext commandContext, String key) {

        String[] splitKey = key.split(Pattern.quote("."));

        if(splitKey.length != 2) {
            try {
                throwSyntaxError(Integer.toString(splitKey.length));
            } catch (LocaliserSyntaxException ex) {
                ex.printStackTrace();
            }
        }

        Gravity gravity = DContainer.INSTANCE.getGravity();
        String userLang = gravity.load(new LangData())
                .get(commandContext.getMember().getUser().getId())
                .defaulting("en") // Defaults language used to be English
                .asString();

        /*
        * File path relative to compiled jar location.
        * Path accesses json file based off of user's prefered language and the first
        * half of the 'key' parameter.
        */
        String pathToLanguageFile = String.format("bot/docker/lang/%s/%s.json", userLang, splitKey[0]);
        StringBuilder jsonBuilder = new StringBuilder("");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(pathToLanguageFile));
            String line = reader.readLine();
            while(line != null) {
                jsonBuilder.append(line);
                line = reader.readLine();
            }
            reader.close();

        } catch(IOException ex) {
            ex.printStackTrace();
        }

        String finalJSON = jsonBuilder.toString();
        if(finalJSON.equals("")) {
            try {
                throwLocalisationError(key);
            } catch (LocalisationException ex) {
                ex.printStackTrace();
            }
        }

        JSON languageFile = new JSON(finalJSON);
        String translation = languageFile.string(splitKey[1]);

        if(translation == null) {
            try {
                throwLocalisationError(key);
            } catch (LocalisationException ex) {
                ex.printStackTrace();
            }
        }

        return translation;

        //TODO: Add startup task that caches all english translations to Redis
    }

    public static String[] getAllLangauges() {
        return new String[]{"en", "es"};
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
