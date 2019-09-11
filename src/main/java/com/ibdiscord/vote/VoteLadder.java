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
import com.ibdiscord.data.db.entries.voting.VoteEntryData;
import com.ibdiscord.data.db.entries.voting.VoteLadderData;
import com.ibdiscord.data.db.entries.voting.VoteListData;
import de.arraying.gravity.Gravity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public final class VoteLadder {

    private final Guild guild;
    private final String name;

    /**
     * Creates a vote ladder.
     * @param guild The guild.
     * @param name The ladder name.
     */
    public VoteLadder(Guild guild, String name) {
        this.guild = guild;
        this.name = name;
    }

    /**
     * Creates a new vote for that ladder.
     * @param text The text.
     * @return The vote entry as an object.
     */
    public synchronized VoteEntry createVote(String text) {
        Gravity gravity = DataContainer.INSTANCE.getGravity();
        VoteListData voteListData = gravity.load(new VoteListData(guild.getId(), name));
        long newId = voteListData.size() + 1;
        VoteLadderData voteLadderData = gravity.load(new VoteLadderData(guild.getId(), name));
        TextChannel channel = guild.getTextChannelById(voteLadderData.get(VoteLadderData.CHANNEL)
                .defaulting(0)
                .asLong());
        if(channel == null) {
            return null;
        }
        long expiry = (voteLadderData.get(VoteLadderData.TIMEOUT)
                .defaulting(12 * 60 * 60 * 1000) // 12 hours
                .asLong())
                + System.currentTimeMillis();
        Message messageObject = channel.sendMessage(newId + ") " + text).complete();
        messageObject.addReaction("\uD83D\uDC4D").queue();
        messageObject.addReaction("\uD83D\uDC4E").queue();
        long message = messageObject.getIdLong();
        VoteEntryData voteEntryData = gravity.load(new VoteEntryData(guild.getId(), name, newId));
        voteEntryData.set(VoteEntryData.EXPIRY, expiry);
        voteEntryData.set(VoteEntryData.MESSAGE, message);
        gravity.save(voteEntryData);
        voteListData.add(newId);
        gravity.save(voteListData);
        VoteEntry entry = new VoteEntry(guild.getId(), name, newId);
        entry.load();
        entry.scheduleStart();
        VoteCache.INSTANCE.register(message, entry);
        return entry;
    }

}
