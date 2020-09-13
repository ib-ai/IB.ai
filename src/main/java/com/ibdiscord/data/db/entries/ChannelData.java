/* Copyright 2020 Nathaneal Varghese
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

package com.ibdiscord.data.db.entries;

import de.arraying.gravity.data.types.TypeMap;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class ChannelData extends TypeMap {

    private final String guildid;
    private final String channeltype;

    /**
     * Gets the identifier.
     * @return The identifier.
     */
    protected String getUniqueIdentifier() {
        return "channeldata_" + guildid + "_" + channeltype;
    }
}
