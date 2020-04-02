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

package com.ibdiscord.data.db.entries.monitor;

import de.arraying.gravity.data.types.TypeMap;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class MonitorData extends TypeMap {

    /**
     * Whether or not the monitor is enabled.
     */
    public static final String ENABLED = "enabled";

    /**
     * The channel to log user monitors in.
     */
    public static final String USER_CHANNEL = "channel_user";

    /**
     * The channel to log message monitors in.
     */
    public static final String MESSAGE_CHANNEL = "channel_message";

    private final String guild;

    /**
     * Gets the unique identifier.
     * @return The identifier.
     */
    @Override
    protected String getUniqueIdentifier() {
        return "monitor_" + guild;
    }

}
