/* Copyright 2018-2020 Arraying
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

package com.ibdiscord.utils;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public final class UInput {

    /*
    * Visual examples:
    * User mention:    <@!152832260310040576>
    * Role mention:    <@&665136133176426496>
    * Channel mention: <#665136115006570507>
    */

    /**
     * The regular expression for a regular user ID.
     */
    private static final Pattern ID_PATTERN = Pattern.compile("^\\d{17,20}$");

    /**
     * The regular expression for a mention.
     */
    private static final Pattern MEMBER_MENTION_PATTERN = Pattern.compile("^<@!?\\d{17,20}>$");

    /**
     * The regular expression for a role.
     */
    private static final Pattern ROLE_MENTION_PATTERN = Pattern.compile("^<@&?\\d{17,20}>$");

    /**
     * The regular expression for a channel.
     */
    private static final Pattern CHANNEL_MENTION_PATTERN = Pattern.compile("^<#?\\d{17,20}>$");

    /**
     * The regular expression for a username#discriminator combination.
     */
    private static final Pattern NAME_PATTERN = Pattern.compile("^.{2,32}#[0-9]{4}$");

    /**
     * The regular expression for a non-universal quotation mark.
     */
    private static final Pattern QUOTATION_MARK = Pattern.compile("[“”„«»]");

    /**
     * Gets the member corresponding to the given user input.
     * @param guild The guild.
     * @param input The input.
     * @return A Member object, or null if the input is invalid.
     */
    public static Member getMember(Guild guild, String input) {
        if(ID_PATTERN.matcher(input).find()
                || MEMBER_MENTION_PATTERN.matcher(input).find()) {
            return guild.getMemberById(input.replaceAll("\\D", "")); // Remove all non-digits.
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
     * Gets the role corresponding to the given user input.
     * @param guild The guild.
     * @param input The input.
     * @return A Role object, or null if the input is invalid.
     */
    public static Role getRole(Guild guild, String input) {
        if(ID_PATTERN.matcher(input).find()
                || ROLE_MENTION_PATTERN.matcher(input).find()) {
            return guild.getRoleById(input.replaceAll("\\D", "")); // Remove all non-digits.
        } else {
            return guild.getRoles().stream()
                    .filter(role -> role.getName().equalsIgnoreCase(input))
                    .findFirst()
                    .orElse(null);
        }
    }

    /**
     * Gets the channel corresponding to the given user input.
     * @param guild The guild.
     * @param input The input.
     * @return A TextChannel object, or null if the input is invalid.
     */
    public static TextChannel getChannel(Guild guild, String input) {
        if(ID_PATTERN.matcher(input).find()
                || CHANNEL_MENTION_PATTERN.matcher(input).find()) {
            return guild.getTextChannelById(input.replaceAll("\\D", "")); // Remove all non-digits.
        } else {
            return guild.getTextChannels().stream()
                    .filter(channel -> channel.getName().equalsIgnoreCase(input))
                    .findFirst()
                    .orElse(null);
        }
    }

    /**
     * Checks whether a String matches the Discord ID pattern.
     * @param input The value to check.
     * @return A boolean depicting whether or not the input is a valid Discord ID.
     */
    public static boolean isValidID(String input) {
        return ID_PATTERN.matcher(input).find();
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
            argument = QUOTATION_MARK.matcher(argument).replaceAll("\"");
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
