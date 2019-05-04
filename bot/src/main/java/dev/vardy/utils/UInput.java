/**
 * Copyright 2017-2019 Arraying
 *
 * This file is part of LoyalBot.
 *
 * LoyalBot is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LoyalBot is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LoyalBot. If not, see http://www.gnu.org/licenses/.
 */

package dev.vardy.utils;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public final class UInput {

    /**
     * The regular expression for a regular user ID.
     */
    private static final Pattern ID_PATTERN = Pattern.compile("^\\d{17,20}$");

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

    /**
     * Extracts quoted strings.
     * @param arguments An array of arguments.
     * @return A set, where each entry is one quoted string, without the quotations.
     */
    public static List<String> extractQuotedStrings(String[] arguments) {
        List<String> output = new ArrayList<>();
        StringBuilder current = null;
        for(String argument : arguments) {
            if(argument.startsWith("\"") && current == null) {
                current = new StringBuilder();
            }
            if(current != null) {
                current.append(argument)
                    .append(" ");
            }
            if(argument.endsWith("\"") && current != null) {
                int index = argument.length() - 2;
                if(index < 0 || argument.charAt(index) != '\\') {
                    String data = current.toString().trim();
                    data = data.substring(1);
                    data = data.substring(0, data.length() - 1);
                    output.add(data);
                    current = null;
                }
            }
        }
        return output;
    }

    /**
     * Tests a regular expression by compiling it, and seeing if it is valid.
     * @param regex The regular expression as a string.
     * @return True if it is, false otherwise.
     */
    public static boolean isValidRegex(String regex) {
        try {
            Pattern.compile(regex);
            return true;
        } catch(PatternSyntaxException exception) {
            return false;
        }
    }

}
