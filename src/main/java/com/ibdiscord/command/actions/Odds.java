/* Copyright 2017-2020 Arraying, Jarred Vardy
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

package com.ibdiscord.command.actions;

import com.ibdiscord.command.CommandAction;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.odds.OddsManager;

public final class Odds implements CommandAction {

    /**
     * Executes the command.
     * @param context The command context.
     */
    @Override
    public void accept(CommandContext context) {
        context.assertArguments(1, "error.generic_arg_length");
        String argument = context.getArguments()[0];
        if(argument.equalsIgnoreCase("cancel")) {
            OddsManager.INSTANCE.cancelBet(context, context.getMember().getId());
            return;
        }

        if(context.getMessage().getMentionedMembers().isEmpty()) {
            context.replyI18n("error.odds_user");
            return;
        }

        // If user is challenging itself
        if(context.getMember().getId().equals(context.getMessage().getMentionedUsers().get(0).getId())) {
            context.replyI18n("error.oddball");
            return;
        }

        int odds = context.assertInt(argument, 1, null, "error.odds_number");

        String userAID = context.getMember().getId();
        String userBID = context.getMessage().getMentionedMembers().get(0).getId();
        OddsManager.INSTANCE.newBet(context, userAID, userBID, odds);
    }

}
