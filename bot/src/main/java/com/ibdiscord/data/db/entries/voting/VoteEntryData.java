package com.ibdiscord.data.db.entries.voting;

import de.arraying.gravity.GravityProvider;
import de.arraying.gravity.data.types.TypeMap;
import lombok.AllArgsConstructor;

/**
 * Copyright 2017-2019 Arraying
 * <p>
 * This file is part of IB.ai.
 * <p>
 * IB.ai is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * IB.ai is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with IB.ai. If not, see http://www.gnu.org/licenses/.
 */
public @AllArgsConstructor final class VoteEntryData extends TypeMap {

    /**
     * The actual voting text.
     */
    public static final String TEXT = "text";

    /**
     * The number of positive votes.
     * Also, for the record, positivity is absolutely overrated.
     */
    public static final String POSITIVE = "youcandoit";

    /**
     * The number of negative votes.
     */
    public static final String NEGATIVE = "no";

    /**
     * The time in milliseconds when the vote expires.
     */
    public static final String EXPIRY = "expiry";

    /**
     * Whether or not the vote has been completed.
     */
    public static final String FINISHED = "finished";

    private final String guild;
    private final String ladder;
    private final long id;

    /**
     * Gets the unique identifier.
     * @return The unique identifier.
     */
    @Override
    protected String getUniqueIdentifier() {
        return "vote_entry_" + guild + "_" + ladder + "_" + id;
    }

    /**
     * Offsets the yes counter by an offset.
     * @param provider The provider.
     * @param offset The offset.
     */
    public void offsetYes(GravityProvider provider, int offset) {
        int now = get(POSITIVE)
                .defaulting(0)
                .asInt();
        now += offset;
        set(POSITIVE, now);
        save(provider);
    }

    /**
     * Offsets the no counter by an offset.
     * @param provider The provider.
     * @param offset The offset.
     */
    public void offsetNo(GravityProvider provider, int offset) {
        int now = get(NEGATIVE)
                .defaulting(0)
                .asInt();
        now += offset;
        set(NEGATIVE, now);
        save(provider);
    }

}
