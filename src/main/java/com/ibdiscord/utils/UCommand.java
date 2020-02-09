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

package com.ibdiscord.utils;

import com.ibdiscord.command.Command;

import java.util.Set;

public final class UCommand {

    /**
     * Searches for a command that meets the query (name or alias is equal to it).
     * @param from The set of commands to query.
     * @param query The actual query (name).
     * @return A command, or null if there is none.
     */
    public static Command query(Set<Command> from, String query) {
        final String search = query.toLowerCase();
        return from.stream().filter(it ->
                it.getName().equals(search) || it.getAliases().contains(search))
                .findFirst()
                .orElse(null);
    }

}
