/* Copyright 2017-2020 Arraying, Jarred Vardy
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

package com.ibdiscord.i18n;

import com.ibdiscord.IBai;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.LangData;
import de.arraying.gravity.Gravity;
import de.arraying.kotys.JSON;
import de.arraying.kotys.JSONArray;
import net.dv8tion.jda.api.entities.User;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum LocaliserHandler {

    /**
     * The singleton instance.
     */
    INSTANCE;

    private final Map<String, Locale> locales = new HashMap<>();
    private final Pattern variablesRegex = Pattern.compile("\\{(\\d+)}", Pattern.MULTILINE);
    private final String defaultLanguage = "en";

    /**
     * Initialize all languages.
     * @param root The root file.
     * @throws IOException If there is any major file exception.
     */
    public void initialize(File root) throws IOException {
        JSON available = new JSON(new File(root, "available_languages.json"));
        JSONArray languages = available.array("languages");
        for(int i = 0; i < languages.length(); i++) {
            JSON language = languages.json(i);
            String name = language.string("language");
            if(name == null) {
                throw new LocaleException("language name not specified");
            }
            String code = language.string("language-code");
            if(code == null) {
                throw new LocaleException("language code not specified");
            }
            String flag = language.string("flag");
            if(flag == null) {
                throw new LocaleException("language flag not specified");
            }
            String encoding = language.string("encoding");
            if(encoding == null) {
                encoding = "UTF-8";
            }
            Locale locale = new Locale(new File(root, code), name, code, flag, encoding);
            locales.put(code, locale);
            IBai.INSTANCE.getLogger().info("Registered language {}", name);
        }
    }

    /**
     * Gets all locales.
     * @return A collection of locales.
     */
    public Collection<Locale> locales() {
        return locales.values();
    }

    /**
     * Translates a string to the user's language.
     * @param context The command context.
     * @param key The key.
     * @param format The formatting variables.
     * @return A never null string.
     */
    public String translate(CommandContext context, String key, Object... format) {
        return translateWithUser(context.getMember().getUser(), key, (Object[]) format);
    }

    /**
     * Translates a string to the user's language.
     * @param user The user
     * @param key The key.
     * @param format The formatting variables.
     * @return A never null string.
     */
    public String translateWithUser(User user, String key, Object... format) {
        String fallback = "**Translation error.** If you see this, please report it to a bot developer!";
        try {
            String language = getUserLanguage(user);
            String value = translateSpecific(language, key, (Object[]) format); // Try with user language.
            if(value == null) {
                value = translateSpecific(defaultLanguage, key, (Object[]) format); // Try with default language.
            }
            if(value == null) {
                value = fallback; // Fallback to error.
            }
            return value;
        } catch(LocaleException exception) {
            exception.printStackTrace();
            return fallback;
        }
    }

    /**
     * Gets all command aliases.
     * @param key The key.
     * @return A never empty set of aliases.
     */
    public Set<String> getCommandAliases(String key) {
        Set<String> aliases = new HashSet<>();
        locales.values()
                .forEach(locale -> {
                    String values = locale.lookup("command_aliases." + key);
                    if(values != null) {
                        aliases.addAll(Arrays.asList(values.split(",")));
                    }
                });
        return aliases;
    }

    /**
     * Formats variables.
     * @param in The string to format.
     * @param format The variables.
     * @return A formatted string.
     */
    public String format(String in, Object... format) {
        final Matcher matcher = variablesRegex.matcher(in);

        /* group(0) --> entire var, {12}
         * group(1) --> just num  , 12
         */
        while(matcher.find()) {
            String toReplace = matcher.group(0);
            String replaceNum = matcher.group(1);

            try {
                in = in.replace(toReplace, format[Integer.parseInt(replaceNum)].toString());
            } catch(ArrayIndexOutOfBoundsException exception) {
                throw new LocaleException(exception);
            }
        }

        return in;
    }


    /**
     * Raw method to get a language by code.
     * @param language The language code.
     * @param key The key to look up.
     * @param format The formatting variables.
     * @return A formatted string, or null.
     */
    private String translateSpecific(String language, String key, Object... format) {
        Locale locale = locales.get(language);
        if(locale == null) {
            return null;
        }
        String value = locale.lookup(key);
        if(value == null) {
            return null;
        }
        return format(value, (Object[]) format);
    }

    /**
     * Gets a user's language, never null.
     * @param user The user.
     * @return The language, "en" by default.
     */
    private String getUserLanguage(User user) {
        Gravity gravity = DataContainer.INSTANCE.getGravity();
        return gravity.load(new LangData())
                .get(user.getId())
                .defaulting(defaultLanguage) // Defaults language used to be English
                .asString();
    }

}
