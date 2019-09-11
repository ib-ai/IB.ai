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

package com.ibdiscord.vote;

import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.DataProvider;
import com.ibdiscord.data.db.entries.voting.VoteEntryData;
import com.ibdiscord.data.db.entries.voting.VoteLadderData;
import com.ibdiscord.startup.tasks.StartBot;
import de.arraying.gravity.Gravity;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@RequiredArgsConstructor
public final class VoteEntry {

    private static ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    private final String guild;
    private final String ladder;
    private final long id;
    private long expiry;
    private boolean finished;
    private ScheduledFuture<?> future;

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
     * Starts the expiration scheduler.
     */
    public void scheduleStart() {
        long difference = expiry - System.currentTimeMillis();
        difference = difference < 0 ? 0 : difference; // So that if the bot is offline there won't be any tasks missed.
        future = scheduledExecutorService.schedule(this::meetsFinalCriteria, difference, TimeUnit.MILLISECONDS);
    }

    /**
     * Cancels the expiration scheduler.
     */
    private void scheduleStop() {
        if(future != null) {
            future.cancel(true);
        }
    }

    /**
     * Gets the ID.
     * @return The ID.
     */
    public long getId() {
        return id;
    }

    /**
     * Adds a yes vote.
     */
    public void voteYes() {
        withData(entry -> entry.offsetYes(new DataProvider(), 1));
        meetsFinalCriteria();
    }

    /**
     * Removes a yes vote.
     */
    public void unvoteYes() {
        withData(entry -> entry.offsetYes(new DataProvider(), -1));
        meetsFinalCriteria();
    }

    /**
     * Adds a no vote.
     */
    public void voteNo() {
        withData(entry -> entry.offsetNo(new DataProvider(), 1));
        meetsFinalCriteria();
    }

    /**
     * Removes a no vote.
     */
    public void unvoteNo() {
        withData(entry -> entry.offsetNo(new DataProvider(), -1));
        meetsFinalCriteria();
    }

    /**
     * Checks whether or no the final criteria is met.
     */
    private synchronized void meetsFinalCriteria() {
        VoteLadderData ladderData = DataContainer.INSTANCE.getGravity().load(new VoteLadderData(guild, ladder));
        withData(entry -> {
            if(entry.get(VoteEntryData.FINISHED).defaulting(false).asBoolean()) {
                return;
            }
            int yes = entry.get(VoteEntryData.POSITIVE)
                    .defaulting(0)
                    .asInt();
            int no = entry.get(VoteEntryData.NEGATIVE)
                    .defaulting(0)
                    .asInt();
            int threshold = ladderData.get(VoteLadderData.THRESHOLD)
                    .asInt();
            if(yes >= threshold || no >= threshold || System.currentTimeMillis() > expiry) {
                long channel = ladderData.get(VoteLadderData.CHANNEL)
                        .defaulting(0)
                        .asLong();
                TextChannel textChannel = StartBot.getJda().getTextChannelById(channel);
                String text;
                if(yes > no) {
                    text = "passed";
                } else if(no > yes) {
                    text = "failed";
                } else {
                    text = "drew";
                }
                if(textChannel != null) {
                    textChannel.sendMessage("Update on vote `"  + ladder + "/" + id + "`: " + text + ".").queue();
                }
                entry.set(VoteEntryData.FINISHED, true);
                entry.save(new DataProvider());
                scheduleStop();
            }
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
