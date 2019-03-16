package com.ibdiscord.utils;

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
        var builder = new StringBuilder();
        for(int i = start; i < input.length; i++) {
            builder.append(input[i])
                    .append(delim);
        }
        var result = builder.toString();
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
