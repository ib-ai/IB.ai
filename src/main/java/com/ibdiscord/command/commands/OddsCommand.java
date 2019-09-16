/* Copyright 2017-2019 Jarred Vardy
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
import com.ibdiscord.odds.OddsManager;

import java.util.Set;

public final class OddsCommand extends Command {

    /**
     * Creates the command.
     */
    public OddsCommand() {
        super("odds",
                Set.of("odd", "wtfarethebloodyoddsgoodsir"),
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

        if(context.getArguments()[0].equals("cancel")) {
            OddsManager.cancelBet(context, context.getMember().getId());
            return;
        }

        if(context.getMessage().getMentionedMembers().isEmpty()) {
            context.reply("Please mention a user to challenge.");
            return;
        }

        // If user is challenging itself
        if(context.getMember().getId().equals(context.getMessage().getMentionedUsers().get(0).getId())) {
            context.reply("Nice try bud.");
            return;
        }

        int odds = 0;
        try {
            odds = Integer.parseInt(context.getArguments()[0]);
        } catch(NumberFormatException ex) {
            context.reply("Please provide an integer greater than zero as the first argument.");
            return;
        }
        if(!(odds > 0)) {
            return;
        }

        String userAID = context.getMember().getId();
        String userBID = context.getMessage().getMentionedMembers().get(0).getId();
        int probability = odds;
        OddsManager.newBet(context, userAID, userBID, probability);
    }
}
