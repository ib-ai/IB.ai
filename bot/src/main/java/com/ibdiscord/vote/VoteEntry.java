package com.ibdiscord.vote;

import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.DataProvider;
import com.ibdiscord.data.db.entries.voting.VoteEntryData;
import de.arraying.gravity.Gravity;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

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
public final @RequiredArgsConstructor class VoteEntry {

    private final String guild;
    private final String ladder;
    private final long id;
    private long expiry;
    private boolean finished;

    /**
     * Loads the data from the database.
     */
    public void load() {
        withData(entry -> {
            expiry = entry.get(VoteEntryData.EXPIRY)
                    .defaulting(0)
                    .asLong();
            finished = entry.get(VoteEntryData.FINISHED)
                    .defaulting(false)
                    .asBoolean();
        });
    }

    /**
     * Saves the current data.
     */
    public void save() {
        withData(entry -> {
            entry.set(VoteEntryData.EXPIRY, expiry);
            entry.set(VoteEntryData.FINISHED, finished);
            entry.save(new DataProvider());
        });
    }

    /**
     * Gets the expiry of the vote.
     * @return The expiry time.
     */
    public long getExpiry() {
        return expiry;
    }

    /**
     * Adds a yes vote.
     */
    public void voteYes() {
        withData(entry -> entry.offsetYes(new DataProvider(), 1));
    }

    /**
     * Removes a yes vote.
     */
    public void unvoteYes() {
        withData(entry -> entry.offsetYes(new DataProvider(), -1));
    }

    /**
     * Adds a no vote.
     */
    public void voteNo() {
        withData(entry -> entry.offsetNo(new DataProvider(), 1));
    }

    /**
     * Removes a no vote.
     */
    public void unvoteNo() {
        withData(entry -> entry.offsetNo(new DataProvider(), -1));
    }

    /**
     * Completes the vote. Executes all tasks and then finishes.
     */
    public void finish() {
        withData(entry -> {
            // TODO tasks here
            entry.save(new DataProvider());
        });
    }

    /**
     * Gets the vote entry data and does something.
     * @param consumer The consumer.
     */
    private void withData(Consumer<VoteEntryData> consumer) {
        Gravity gravity = DataContainer.INSTANCE.getGravity();
        consumer.accept(gravity.load(new VoteEntryData(guild, ladder, id)));
    }

}
