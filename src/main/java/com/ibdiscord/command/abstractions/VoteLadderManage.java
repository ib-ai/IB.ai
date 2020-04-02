/* Copyright 2017-2020 Arraying
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

package com.ibdiscord.command.abstractions;

import com.ibdiscord.command.CommandAction;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.voting.VoteLadderData;
import com.ibdiscord.data.db.entries.voting.VoteLaddersData;
import de.arraying.gravity.Gravity;

public abstract class VoteLadderManage implements CommandAction {

    /**
     * Handles the actual data setting part of the command.
     * @param context The command context.
     * @param ladderData The ladder data.
     */
    protected abstract void handle(CommandContext context, VoteLadderData ladderData);

    /**
     * Does the core job of the data command.
     * @param context The command context.
     */
    @Override
    public final void accept(CommandContext context) {
        context.assertArguments(1, "error.ladder_name");
        String ladder = context.getArguments()[0].toLowerCase();
        Gravity gravity = DataContainer.INSTANCE.getGravity();
        VoteLaddersData laddersData = gravity.load(new VoteLaddersData(context.getGuild().getId()));
        if(!laddersData.contains(ladder)) {
            context.replyI18n("error.ladder_noexist");
            return;
        }
        VoteLadderData ladderData = gravity.load(new VoteLadderData(context.getGuild().getId(), ladder));
        handle(context, ladderData);
        gravity.save(ladderData);
    }

}
