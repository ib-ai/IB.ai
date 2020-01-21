/* Copyright 2017-2019 Arraying
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

public final class UString {

    /**
     * Concatenates a string array.
     * @param input The string array.
     * @param delim The delimiter.
     * @param start The start index.
     * @return A concatenated string.
     */
    public static String concat(String[] input, String delim, int start) {
        if(input == null) {
            throw new IllegalArgumentException("input null");
        }
        if(start >= input.length) {
            throw new IllegalArgumentException("start > max index");
        }
        if(delim == null) {
            delim = "";
        }
        StringBuilder builder = new StringBuilder();
        for(int i = start; i < input.length; i++) {
            builder.append(input[i])
                    .append(delim);
        }
        String result = builder.toString();
        if(result.endsWith(delim)) {
            result = result.substring(0, result.length() - delim.length());
        }
        return result;
    }

    /**
     * Turns a string into a long.
     * @param input The string.
     * @return Null if error, otherwise a long.
     */
    public static Long toLong(String input) {
        try {
            return Long.valueOf(input);
        } catch(IllegalArgumentException exception) {
            return null;
        }
    }

    /**
     * Strips all mass mentions.
     * @param input The input.
     * @return Input with all the mass mentions stripped.
     */
    public static String stripMassMentions(String input) {
        if(input == null) {
            throw new IllegalArgumentException("input null");
        }
        return input
                .replace("@everyone", "@\u200Beveryone")
                .replace("@here", "@\u200Bhere");
    }

    /**
     * Escapes all formatting.
     * @param input The input.
     * @return Input with all formatting escaped.
     */
    public static String escapeFormatting(String input) {
        if(input == null) {
            throw new IllegalArgumentException("input null");
        }
        return input.replace("*", "\\*")
                .replace("_", "\\_")
                .replace("~", "\\~")
                .replace("`", "\\`");
    }

    /**
     * Simple truncation of string, using an ellipses to cut a given string input down to a given length.
     * @param length The string to truncate.
     * @return The truncated string.
     */
    public static String truncate(String input, int length) {
        if(input.length() <= length) { // Roses are red
            return input;              // Violets are blue
        }                              // What a fucking oversight
        String symbol = "...";         // My skills are poo
        int cutDown = symbol.length() + 1;
        return input.substring(0, length - cutDown) + symbol;
    }

    /**
     * Truncates a string to a given length, inserting a given truncation at the end. Overload for default
     * ellipses truncation. Examples:
     *
     * #("Hello-there-world, 8, 3, TruncationSymbol.ELLIPSES") ->
     *   "He...rld" (total of 8 length)
     *
     * #("Hello-there-world, 5, 0, TruncationSymbol.HYPHEN") ->
     *   "Hell-" (total of 5 length)
     *
     * @param length The string to truncate.
     * @param loopback The number of characters to leave at the end of the truncated string. See example.
     * @return The truncated string.
     */
    public static String truncate(String input, int length, int loopback, TruncationSymbol symbol) {
        if(input.length() <= length) {
            return input;
        }
        String ending;
        switch (symbol) {
            case ELLIPSES: ending = "...";
                break;
            case HYPHEN: ending = "-";
                break;
            case NONE: ending = "";
                break;
            default: ending = "...";
                break;
        }

        String cap = ending + input.substring(input.length() - loopback);
        int diff = length - cap.length();

        return input.substring(0, diff) + cap;
    }

}
