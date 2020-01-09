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

package com.ibdiscord.data.db.entries.voting;

import de.arraying.gravity.data.types.TypeMap;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class VoteLadderData extends TypeMap {

    /**
     * The ID of the channel where updates will be sent.
     */
    public static final String CHANNEL = "channel";

    /**
     * The threshold for a vote to pass or fail.
     */
    public static final String THRESHOLD = "threshold";

    /**
     * The timeout until the option with the highest number is selected when there is no pass or fail.
     */
    public static final String TIMEOUT = "timeout";

    private final String guild;
    private final String ladder;

    /**
     * Gets the unique identifier.
     * @return The unique identifier.
     */
    @Override
    protected String getUniqueIdentifier() {
        return "vote_ladder_" + guild + "_" + ladder;
    }

}
