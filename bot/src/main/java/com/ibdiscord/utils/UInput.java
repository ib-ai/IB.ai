package com.ibdiscord.utils;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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
public final class UInput {

    /**
     * The regular expression for a regular user ID.
     */
    public static final Pattern ID_PATTERN = Pattern.compile("^\\d{17,20}$");

    /**
     * The regular expression for a mention.
     */
    private static final Pattern MENTION_PATTERN = Pattern.compile("^<@!?\\d{17,20}>$");

    /**
     * The regular expression for a username#discriminator combination.
     */
    private static final Pattern NAME_PATTERN = Pattern.compile("^.{2,32}#[0-9]{4}$");

    /**
     * Gets the member corresponding to the given user input.
     * @param guild The guild.
     * @param input The input.
     * @return A Member object, or null if the input is invalid.
     */
    public static Member getMember(Guild guild, String input) {
        if(ID_PATTERN.matcher(input).find()
                || MENTION_PATTERN.matcher(input).find()) {
            return guild.getMemberById(input.replaceAll("\\D", ""));
        } else if(NAME_PATTERN.matcher(input).find()) {
            String name = input.substring(0, input.indexOf("#"));
            String discriminator = input.substring(input.indexOf("#") + 1);
            return guild.getMembers().stream()
                    .filter(user -> user.getUser().getName().equalsIgnoreCase(name)
                            && user.getUser().getDiscriminator().equals(discriminator))
                    .findFirst()
                    .orElse(null);
        } else {
            return null;
        }
    }

    public static List<String> extractQuotedStrings(String[] arguments) {
        List<String> output = new ArrayList<>();
        StringBuilder current = null;
        for(String argument : arguments) {
            if(argument.startsWith("\"") && current == null) {
                current = new StringBuilder();
                continue;
            }
            if(argument.endsWith("\"") && current != null) {
                int index = argument.length() - 2;
                if(index < 0 || argument.charAt(index) != '\\') {
                    output.add(current.toString());
                    current = null;
                    continue;
                }
            }
            if(current != null) {
                current.append(argument);
            }
        }
        return output;
    }

}
