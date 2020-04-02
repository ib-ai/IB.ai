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

package com.ibdiscord.utils.objects;

import com.ibdiscord.command.Command;

public final class Comparator implements java.util.Comparator<Command> {

    /**
     * Compares two commands by name in order to present commands alphabetically.
     * @param o1 The first command.
     * @param o2 The second command.
     * @return A comparison integer.
     */
    @Override
    public int compare(Command o1, Command o2) {
        return o1.getName().compareTo(o2.getName());
    }

}
