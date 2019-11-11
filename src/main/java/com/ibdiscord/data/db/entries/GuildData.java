/* Copyright 2017-2019 Arraying
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
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class GuildData extends TypeMap {

    /**
     * The prefix key.
     */
    public static final String PREFIX = "prefix";

    /**
     * The modlogs key for Redis.
     */
    public static final String MODLOGS = "modlogs";

    /**
     * The mute role key.
     */
    public static final String MUTE = "mute_role";

    /**
     * The logs key.
     */
    public static final String LOGS = "logs";

    /**
     * The moderator key.
     */
    public static final String MODERATOR = "moderator";

    /**
     * The helper key.
     */
    public static final String HELPER = "helper";

    /**
     * Whether or not filtering is enabled.
     */
    public static final String FILTERING = "filtering";

    /**
     * The filter removal message.
     */
    public static final String REMOVAL = "removal";

    private final String guild;

    /**
     * Gets the identifier.
     * @return The identifier.
     */
    @Override
    protected String getUniqueIdentifier() {
        return "guild_" + guild;
    }

}
