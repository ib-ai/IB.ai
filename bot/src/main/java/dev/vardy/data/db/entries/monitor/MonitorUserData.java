/**
 * Copyright 2017-2019 Arraying
 *
 * This file is part of LoyalBot.
 *
 * LoyalBot is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LoyalBot is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LoyalBot. If not, see http://www.gnu.org/licenses/.
 */

package dev.vardy.data.db.entries.monitor;

import de.arraying.gravity.data.types.TypeSet;

import lombok.AllArgsConstructor;

public final @AllArgsConstructor class MonitorUserData extends TypeSet {

    private final String guild;

    /**
     * Gets the unique identifier.
     * @return The identifier.
     */
    @Override
    protected String getUniqueIdentifier() {
        return "monitor_users_" + guild;
    }

}
