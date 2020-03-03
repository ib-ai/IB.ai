/* Copyright 2017-2020 Arraying
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

import com.ibdiscord.command.CommandContext;

import java.util.Arrays;

public final class StringI18n {

    private final String first;
    private final Object[] following;

    /**
     * Creates an i18n string. This is a string that can both be static or translated.
     *
     * For translated strings:
     * Set <code>first</code> to the key, and <code>following</code> to the formatting elements.
     *
     * For static strings:
     * Set <code>first</code> to <code>null</code> and set the first element of <code>following</code> to the string.
     * Any formatting elements can be defined as further objects in <code>following</code>.
     *
     * @param first See above.
     * @param following See above.
     */
    public StringI18n(String first, Object... following) {
        this.first = first;
        this.following = following;
    }

    /**
     * Parses the string.
     * @param context The command context.
     * @return A string.
     */
    @SuppressWarnings("WeakerAccess")
    public String parse(CommandContext context) {
        if(first == null) {
            if(following.length == 0) {
                throw new IllegalStateException("using static string but format is empty");
            }
            String value = following[0].toString();
            Object[] update = Arrays.copyOfRange(following, 1, following.length);
            return LocaliserHandler.INSTANCE.format(value, (Object[]) update);
        } else {
            return LocaliserHandler.INSTANCE.translate(context, first, (Object[]) following);
        }
    }

}
