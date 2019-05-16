package com.ibdiscord.utils;

/**
 * Copyright 2017-2019 Arraying
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

}
