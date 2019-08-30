package com.ibdiscord.localisation;

import com.ibdiscord.command.CommandContext;
import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.LangData;
import com.ibdiscord.exceptions.LocalisationException;
import com.ibdiscord.exceptions.LocaliserSyntaxException;
import com.ibdiscord.utils.UJSON;
import de.arraying.gravity.Gravity;
import de.arraying.kotys.JSON;
import de.arraying.kotys.JSONArray;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public enum Localiser {

    /**
     *  Singleton instance of Localiser.
     */
    INSTANCE;

    private static final String variablesRegex = "\\{(\\d+)}";

    public void init() {
        //TODO: Cache all english translations to Redis
    }

    /**
     * Localiser. Used to find the correct localisation of a piece of text based off of
     * the inputted user's language preference.
     * @param commandContext The context of the command this method is called from
     * @param key The identifier for the text that is to be found < category.key >
     * @return The localised text corresponding to the inputted key
     */
    public static String __(CommandContext commandContext, String key, String... variables) {

        String[] splitKey = key.split(Pattern.quote("."));

        if(splitKey.length != 2) {
            try {
                throwSyntaxError(Integer.toString(splitKey.length));
            } catch (LocaliserSyntaxException ex) {
                ex.printStackTrace();
            }
        }

        Gravity gravity = DataContainer.INSTANCE.getGravity();
        String userLang = gravity.load(new LangData())
                .get(commandContext.getMember().getUser().getId())
                .defaulting("en") // Defaults language used to be English
                .asString();

        /*
         * Absolute file path relative to lang directory.
         * Path accesses json file based off of user's preferred language and the first
         * half of the 'key' parameter.
         */
        String pathToLanguageFile = String.format("/IB.ai/lang/%s/%s.json", userLang, splitKey[0]);
        JSON languageFile = UJSON.retrieveJSONFromFile(pathToLanguageFile);
        String translation = languageFile.string(splitKey[1]);

        if(translation == null) {
            try {
                throwLocalisationError(key);
            } catch (LocalisationException ex) {
                ex.printStackTrace();
            }
        }

        // Replace variables within translation before returning final translation.
        return replaceVariables(translation, variables);
    }

    private static String replaceVariables(String text, String[] vars) {
        final Pattern pattern = Pattern.compile(variablesRegex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(text);

        /* group(0) --> entire var, {12}
         * group(1) --> just num  , 12
         */
        while(matcher.find()) {
            String toReplace = matcher.group(0);
            String replaceNum = matcher.group(1);

            text = text.replace(toReplace, vars[Integer.parseInt(replaceNum)]);
        }

        return text;
    }

    public static String[] getAllLanguages() {
        String pathToAvailableLanguages = "/IB.ai/lang/available_languages.json";
        JSON allLanguagesFile = UJSON.retrieveJSONFromFile(pathToAvailableLanguages);

        JSONArray arrayOfLanguages = allLanguagesFile.array("languages");
        return (String[]) arrayOfLanguages.toArray();
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
