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

package com.ibdiscord.command.commands.voting.vote;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.GuildData;
import com.ibdiscord.data.db.entries.voting.VoteLaddersData;
import com.ibdiscord.utils.UString;
import com.ibdiscord.vote.VoteEntry;
import com.ibdiscord.vote.VoteLadder;
import de.arraying.gravity.Gravity;

import java.util.Set;

public final class VoteCommand extends Command {

    /**
     * Creates the vote command.
     */
    public VoteCommand() {
        super("vote",
                CommandPermission.role(GuildData.MODERATOR),
                Set.of()
        );
        this.correctUsage = "vote <ladder> <text>";
    }

    /**
     * Starts a vote.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        if(context.getArguments().length < 2) {
            sendUsage(context);
            return;
        }
        String ladder = context.getArguments()[0].toLowerCase();
        Gravity gravity = DataContainer.INSTANCE.getGravity();
        VoteLaddersData laddersData = gravity.load(new VoteLaddersData(context.getGuild().getId()));
        if(!laddersData.contains(ladder)) {
            context.reply("That ladder does not exist.");
            return;
        }
        String text = UString.concat(context.getArguments(), " ", 1);
        VoteLadder voteLadder = new VoteLadder(context.getGuild(), ladder);
        VoteEntry voteEntry = voteLadder.createVote(text);
        if(voteEntry == null) {
            context.reply("Could not create the vote. Is the ladder set up properly, and are the permissions correct?");
            return;
        }
        context.reply("Created vote #" + voteEntry.getId());
    }

}
