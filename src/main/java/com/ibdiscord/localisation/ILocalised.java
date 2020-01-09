/* Copyright 2018-2020 Jarred Vardy
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

package com.ibdiscord.localisation;

import com.ibdiscord.command.CommandContext;

public interface ILocalised {

    /**
     * Interface method alias for Localiser __ method.
     * @param commandContext The context of the command this method is called from.
     * @param key The identifier for the text that is to be found < category.key >.
     * @param variables Ordered variables to be substituted into final translation.
     * @return The localised text corresponding to the inputted key.
     */
    default String __(CommandContext commandContext, String key, String... variables) {
        return Localiser.__(commandContext, key, variables);
    }
}
