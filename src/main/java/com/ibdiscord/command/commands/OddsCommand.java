/* Copyright 2018-2020 Jarred Vardy
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

package com.ibdiscord.command.commands;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.localisation.Localiser;
import com.ibdiscord.odds.OddsManager;

import java.util.Set;

public final class OddsCommand extends Command {

    /**
     * Creates the command.
     */
    public OddsCommand() {
        super("odds",
                CommandPermission.discord(),
                Set.of()
        );
        this.correctUsage = "odds <cancel|probability> [@User]";
    }

    @Override
    protected void execute(CommandContext context) {
        if(context.getArguments().length == 0) {
            sendUsage(context);
            return;
        }

        Set<String> cancelAliases = Localiser.getAllCommandAliases("command_aliases.odds_cancel");
        if(cancelAliases.contains(context.getArguments()[0])) {
            OddsManager.cancelBet(context, context.getMember().getId());
            return;
        }

        if(context.getMessage().getMentionedMembers().isEmpty()) {
            context.reply(__(context, "error.odds_user"));
            return;
        }

        // If user is challenging itself
        if(context.getMember().getId().equals(context.getMessage().getMentionedUsers().get(0).getId())) {
            context.reply(__(context, "error.oddball"));
            return;
        }

        int odds;
        try {
            odds = Integer.parseInt(context.getArguments()[0]);
        } catch(NumberFormatException ex) {
            context.reply(__(context, "error.odds_number"));
            return;
        }
        if(odds <= 0) {
            return;
        }

        String userAID = context.getMember().getId();
        String userBID = context.getMessage().getMentionedMembers().get(0).getId();
        int probability = odds;
        OddsManager.newBet(context, userAID, userBID, probability);
    }
}
