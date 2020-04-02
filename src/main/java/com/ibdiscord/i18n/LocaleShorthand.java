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

public interface LocaleShorthand {

    /**
     * Shorthand localisation method.
     * @param context The command context.
     * @param key The key.
     * @param format The format.
     * @return The translated string.
     */
    default String __(CommandContext context, String key, Object... format) {
        return LocaliserHandler.INSTANCE.translate(context, key, (Object[]) format);
    }

}
