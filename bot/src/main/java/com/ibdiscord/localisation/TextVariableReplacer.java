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

import java.util.HashMap;
import java.util.function.Function;

public final class TextVariableReplacer {

    public static String replaceVariables(String text) {
        String[] variables = null;
        //TODO: Get variables with regex from text
        // For each variable, get corresponding Function from hashmap of variables
        // Call function and pass in CommandContext and String.replace() the variables from the text.

        //Funtion<CommandContext, String> replacerFunction = getVariableList().get()
        return "";
    }

    public static HashMap<String, Function<CommandContext, String>> getVariableList() {

        HashMap<String, Function<CommandContext, String>> variableHash = new HashMap<>();

        Function<CommandContext, String> userName = (context) -> context.getMember().getUser().getName();
        variableHash.put("userName", userName);

        //Function<CommandContext, String> name = (context) -> ;
        //variableList.add(new Tuple<>("", name));

        return variableHash;
    }
}
