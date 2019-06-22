/**
 * Copyright 2017-2019 Jarred Vardy <jarred.vardy@gmail.com>
 * <p>
 * This file is part of IB.ai.
 * <p>
 * IB.ai is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * IB.ai is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with IB.ai. If not, see http://www.gnu.org/licenses/.
 */

package com.ibdiscord.localisation;

import com.ibdiscord.command.CommandContext;
import lombok.Getter;

import java.util.HashMap;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TextVariableReplacer {

    @Getter
    private static HashMap<String, Function<CommandContext, String>> variableMap = null;

    private static final String variablesRegex = ":(\\w*)";

    static String replaceVariables(CommandContext commandContext, String text) {

        if(variableMap == null) {
            generateVariableMap();
        }

        final Pattern pattern = Pattern.compile(variablesRegex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(text);

        // matcher.group(0) -> entire variable and colon
        // matcher.group(1) -> just the variable
        while (matcher.find()) {
            String variable = matcher.group(1);

            if(variableMap.containsKey(variable)) {
                String replacement = variableMap.get(variable).apply(commandContext);
                text = text.replace(variable, replacement);
            }
        }

        return text;
    }

    static void generateVariableMap() {

        if(variableMap != null) {
            return;
        }

        variableMap = new HashMap<>();

        /*
         * Each 'variable' within a translated string (e.g: :userName) is a key
         * in variableMap, called in order to retrieve the corresponding text
         * which is different depending on the context of the user's command.
         *
         * Map of Strings to Function objects. Function objects accept CommandContext objects
         * and return String objects.
         */

        Function<CommandContext, String> userName = (context) -> context.getMember().getUser().getName();
        variableMap.put("userName", userName);

        //TODO: Add the rest of the variables found within strings in the project.
    }
}
