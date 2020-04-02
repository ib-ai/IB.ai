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

package com.ibdiscord.data;

import com.ibdiscord.utils.objects.Tuple;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("WeakerAccess")
public final class LocalSubjects {

    @Getter private Set<Tuple<String, Long>> subjects = new HashSet<>();

    /**
     * Creates an empty subject set.
     */
    LocalSubjects() {
    } // I literally have to declare it like this because of checkstyle, otherwise it won't compile. Bruh.


    /**
     * Creates a populated subject set.
     * @param raw The raw input.
     */
    LocalSubjects(String raw) {
        String[] sets = raw.split(";");
        for(String set : sets) {
            String[] components = set.split(",");
            long id;
            try {
                id = Long.valueOf(components[1]);
            } catch(ArrayIndexOutOfBoundsException | NumberFormatException ignored) {
                id = 0L;
            }
            subjects.add(new Tuple<>(components[0], id));
        }
    }

}
