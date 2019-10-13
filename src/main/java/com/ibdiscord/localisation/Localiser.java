/* Copyright 2017-2019 Jarred Vardy <jarred.vardy@gmail.com>
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
import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.LangData;
import com.ibdiscord.exceptions.LocaliserSyntaxException;
import com.ibdiscord.utils.UJSON;
import de.arraying.gravity.Gravity;
import de.arraying.kotys.JSON;
import de.arraying.kotys.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public enum Localiser {

    /**
     *  Singleton instance of Localiser.
     */
    INSTANCE;

    private static final String variablesRegex = "\\{(\\d+)}";

    /**
     * Initialises localiser. Used to cache English translations for faster
     * retrieval.
     */
    public void init() {
        //TODO: Cache all english translations to Redis
    }

    /**
     * Localiser. Used to find the correct localisation of a piece of text based off of
     * the inputted user's language preference.
     * @param commandContext The context of the command this method is called from.
     * @param key The identifier for the text that is to be found < category.key >.
     * @param variables Ordered variables to be substituted into final translation.
     * @return The localised text corresponding to the inputted key.
     */
    public static String __(CommandContext commandContext, String key, String... variables) {
        String translationErrorMessage = "**Translation error. If you see this, please report it to "
                + "pants#0422 or Arraying#7363.**";
        String translation;
        try {
            System.out.println("Retrieving " + key);
            JSON languageFile = getJSONObjectFromKey(getUserLang(commandContext), key);
            String[] splitKey = key.split(Pattern.quote("."));
            translation = languageFile.string(splitKey[1]);
        } catch(Exception ex) {
            ex.printStackTrace(); // pantsyboo, don't you want to print the error to be able to fix it?
            return translationErrorMessage;
        }
        if(translation == null) {
            return translationErrorMessage;
        }
        // Replace variables within translation before returning final translation.
        return replaceVariables(translation, variables);
    }

    /**
     * Gets all localised command and sub-command names on file.
     * @param key The identifier of the array to find.
     * @return The localised entry location as a set.
     */
    public static Set<String> getAllCommandAliases(String key) {
        String[] splitKey = key.split(Pattern.quote("."));
        List<String> allAliases = new ArrayList<>();
        getAllLanguageCodes().forEach(lang -> {
            JSON languageFile = getJSONObjectFromKey(lang, key);
            JSONArray arrayOfAliasesObj = languageFile.array(splitKey[1]);
            try {
                allAliases.addAll(Arrays.stream(arrayOfAliasesObj.toArray())
                        .map(Object::toString)
                        .collect(Collectors.toSet()));
            } catch(NullPointerException ex) {
                ex.printStackTrace();
                System.out.println("KEY THROWING ERROR: " + splitKey[1]);
                System.exit(1);
            }
        });
        return new HashSet<>(allAliases);
    }

    /**
     * Gets a user's desired language from the database.
     * @param commandContext Context of the user's command call.
     * @return The user's language preference.
     */
    private static String getUserLang(CommandContext commandContext) {
        Gravity gravity = DataContainer.INSTANCE.getGravity();
        return gravity.load(new LangData())
                .get(commandContext.getMember().getUser().getId())
                .defaulting("en") // Defaults language used to be English
                .asString();
    }

    /**
     * Process of retrieving a JSON object from a valid key and context.
     * @param language The desired language.
     * @param key The identifier of the key to find.
     * @return The JSON object of the file to query.
     */
    private static JSON getJSONObjectFromKey(String language, String key) {
        String[] splitKey = key.split(Pattern.quote("."));

        if(splitKey.length != 2) {
            try {
                throwSyntaxError(Integer.toString(splitKey.length));
            } catch (LocaliserSyntaxException ex) {
                ex.printStackTrace();
            }
        }

        /*
         * Absolute file path relative to lang directory.
         * Path accesses json file based off of user's preferred language and the first
         * half of the 'key' parameter.
         */
        String pathToLanguageFile = String.format("/IB.ai/lang/%s/%s.json", language, splitKey[0]);
        return UJSON.retrieveJSONFromFile(pathToLanguageFile, getLanguageCharset(language));
    }

    /**
     * Replaces variables within a piece of text based off of regex with provided variables.
     * @param text The text containing variables to replace.
     * @param vars The variables to insert into the text.
     * @return The text once all variable locations have been replaced by text.
     */
    private static String replaceVariables(String text, String[] vars) {
        final Pattern pattern = Pattern.compile(variablesRegex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(text);

        /* group(0) --> entire var, {12}
         * group(1) --> just num  , 12
         */
        while(matcher.find()) {
            String toReplace = matcher.group(0);
            String replaceNum = matcher.group(1);

            try {
                text = text.replace(toReplace, vars[Integer.parseInt(replaceNum)]);
            } catch(ArrayIndexOutOfBoundsException ex) {
                ex.printStackTrace();
            }
        }

        return text;
    }

    /**
     * Retrieves available languages from JSON file as their codes.
     * @return Array of all available languages for localisation
     */
    public static List<String> getAllLanguageCodes() {
        String pathToAvailableLanguages = "/IB.ai/lang/available_languages.json";
        JSON allLanguagesFile = UJSON.retrieveJSONFromFile(pathToAvailableLanguages);

        JSONArray arrayOfLanguages = allLanguagesFile.array("languages");
        List<String> languageCodes = new ArrayList<>();
        for(int i = 0; i < arrayOfLanguages.length(); i++) {
            JSON jsonObj = arrayOfLanguages.json(i);
            languageCodes.add(jsonObj.string("language-code").toLowerCase());
        }
        return languageCodes;
    }

    /**
     * Retrieves available languages from JSON file.
     * @return Array of all available languages for localisation
     */
    public static List<String> getAllLanguages() {
        String pathToAvailableLanguages = "/IB.ai/lang/available_languages.json";
        JSON allLanguagesFile = UJSON.retrieveJSONFromFile(pathToAvailableLanguages);

        JSONArray arrayOfLanguages = allLanguagesFile.array("languages");
        List<String> languageCodes = new ArrayList<>();
        for(int i = 0; i < arrayOfLanguages.length(); i++) {
            JSON jsonObj = arrayOfLanguages.json(i);
            languageCodes.add(jsonObj.string("language").toLowerCase());
        }
        return languageCodes;
    }

    /**
     * Retrieves language name from its code.
     * @param lang The language to find the code for.
     * @return The name of the language.
     */
    public static String getLanguageCode(String lang) {
        String pathToAvailableLanguages = "/IB.ai/lang/available_languages.json";
        JSON allLanguagesFile = UJSON.retrieveJSONFromFile(pathToAvailableLanguages);

        JSONArray arrayOfLanguages = allLanguagesFile.array("languages");
        String language = null;
        for(int i = 0; i < arrayOfLanguages.length(); i++) {
            JSON jsonObj = arrayOfLanguages.json(i);
            if(jsonObj.string("language").equalsIgnoreCase(lang)) {
                language = jsonObj.string("language-code");
            }
        }
        return language;
    }

    /**
     * Gets the language charset to use when decoding the file.
     * @param lang The language to find the charset for.
     * @return The charset of the language.
     */
    public static String getLanguageCharset(String lang) {
        String pathToAvailableLanguages = "/IB.ai/lang/available_languages.json";
        JSON allLanguagesFile = UJSON.retrieveJSONFromFile(pathToAvailableLanguages);

        JSONArray arrayOfLanguages = allLanguagesFile.array("languages");
        String charset = null;
        for(int i = 0; i < arrayOfLanguages.length(); i++) {
            JSON jsonObj = arrayOfLanguages.json(i);
            if(jsonObj.string("language-code").equals(lang)) {
                charset = jsonObj.string("encoding");
            }
        }
        return charset;
    }

    /**
     * Retrieves the name of a language based off of its index in
     * the languages array in the available_languages.json file.
     * @param index The index of the language
     * @return The name of the language at the given index
     */
    public static String getLanguageNameByIndex(int index) {
        String pathToAvailableLanguages = "/IB.ai/lang/available_languages.json";
        JSON allLanguagesFile = UJSON.retrieveJSONFromFile(pathToAvailableLanguages);

        JSONArray arrayOfLanguages = allLanguagesFile.array("languages");
        JSON langObj = arrayOfLanguages.json(index);
        return langObj.string("language");
    }

    /**
     * Retrieves the flag belonging to a language based off of its index in
     * the languages array in the available_languages.json file.
     * @param index The index of the language
     * @return The flag emote of the language at the given index
     */
    public static String getLanguageFlagByIndex(int index) {
        String pathToAvailableLanguages = "/IB.ai/lang/available_languages.json";
        JSON allLanguagesFile = UJSON.retrieveJSONFromFile(pathToAvailableLanguages);

        JSONArray arrayOfLanguages = allLanguagesFile.array("languages");
        JSON langObj = arrayOfLanguages.json(index);
        return langObj.string("flag");
    }

    /**
     * Throws a syntax error.
     * @param numOfKeys The number of keys passed to the localiser method.
     * @throws LocaliserSyntaxException Syntax exception.
     */
    private static void throwSyntaxError(String numOfKeys) throws LocaliserSyntaxException {
        throw new LocaliserSyntaxException(numOfKeys);
    }
}
