package com.ibdiscord.command.commands.voting.ladder;

import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.entries.voting.VoteLadderData;
import com.ibdiscord.localisation.Localiser;
import net.dv8tion.jda.core.Permission;

import java.util.Set;

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
public final class VoteLadderChannelCommand extends VoteLadderDataCommand {

    /**
     * Creates the command.
     */
    VoteLadderChannelCommand() {
        super("channel",
                Set.of(),
                CommandPermission.discord(Permission.MANAGE_SERVER),
                Set.of()
        );
    }

    /**
     * Handles the data.
     * @param context The command context.
     * @param ladderData The ladder data.
     */
    @Override
    protected void handle(CommandContext context, VoteLadderData ladderData) {
        if(context.getMessage().getMentionedChannels().isEmpty()) {
            context.reply(Localiser.__(context, "error.missing_channel"));
            return;
        }
        ladderData.set(VoteLadderData.CHANNEL, context.getMessage().getMentionedChannels().get(0).getIdLong());
        context.reply(Localiser.__(context, "succes.channel_update"));
    }

}
