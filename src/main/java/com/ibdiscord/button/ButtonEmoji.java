/* Copyright 2017-2021 Arraying
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

package com.ibdiscord.button;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.utils.data.DataObject;

import java.util.regex.Pattern;

/**
 * Represents a button emoji.
 */
@RequiredArgsConstructor
public final class ButtonEmoji {

    private static Pattern EMOTE_PATTERN = Pattern.compile("^<(?:a)?:(\\S{2,32}):(\\d{17,20})>$");
    private final boolean emoji;
    private final String id;
    private final String name;
    private final boolean animated;

    /**
     * Gets the emoji from a string.
     * @param input The string.
     * @return A possibly null emoji.
     */
    public static ButtonEmoji parse(String input) {
        if (input == null) {
            return null;
        }
        input = input.trim();
        if (EMOTE_PATTERN.matcher(input).matches()) {
            String[] pieces = input.split(":");
            String animated = pieces[1];
            String name = pieces[1];
            String id = pieces[2].replaceAll("\\D", "");
            return new ButtonEmoji(false, id, name, animated.contains("a"));
        } else {
            return new ButtonEmoji(true, null, input, false);
        }
    }

    /**
     * Wraps the emoji as JSON.
     * @return A JSON object.
     */
    public DataObject get() {
        DataObject dataObject = DataObject.empty();
        if (emoji) {
            dataObject.putNull("id");
            dataObject.put("name", name);
        } else {
            dataObject.put("id", id);
            dataObject.put("name", name);
            dataObject.put("animated", animated);
        }
        return dataObject;
    }

    /**
     * Debugging friendly representation.
     * @return The string.
     */
    @Override
    public String toString() {
        return "ButtonEmoji{"
            + "emoji=" + emoji
            + ", id='" + id + '\''
            + ", name='" + name + '\''
            + ", animated=" + animated
            + '}';
    }
}
