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

package com.ibdiscord.vote;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum VoteCache {

    /**
     * The singleton instance.
     */
    INSTANCE;

    private final Map<Long, VoteEntry> votes = new ConcurrentHashMap<>();

    /**
     * Registers a vote per message.
     * @param id The message ID.
     * @param entry The entry.
     */
    public void register(long id, VoteEntry entry) {
        votes.put(id, entry);
    }

    /**
     * Gets a vote by message ID.
     * @param id The message ID.
     * @return The entry, or null if it doesn't exist.
     */
    public VoteEntry get(long id) {
        return votes.get(id);
    }

}
