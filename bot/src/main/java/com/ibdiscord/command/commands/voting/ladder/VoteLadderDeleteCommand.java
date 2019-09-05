package com.ibdiscord.command.commands.voting.ladder;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.voting.VoteLaddersData;
import com.ibdiscord.localisation.Localiser;
import de.arraying.gravity.Gravity;
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
public final class VoteLadderDeleteCommand extends Command {

    /**
     * Creates the command.
     */
    VoteLadderDeleteCommand() {
        super("delete",
                Set.of("d", "remove", "r"),
                CommandPermission.discord(Permission.MANAGE_SERVER),
                Set.of()
        );
    }

    /**
     * Deletes a vote ladder.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        if(context.getArguments().length < 1) {
            context.reply(Localiser.__(context, "error.ladder_name"));
            return;
        }
        String ladder = context.getArguments()[0].toLowerCase();
        Gravity gravity = DataContainer.INSTANCE.getGravity();
        VoteLaddersData laddersData = gravity.load(new VoteLaddersData(context.getGuild().getId()));
        laddersData.remove(ladder);
        gravity.save(laddersData);
        context.reply(Localiser.__(context, "success.ladder_delete", ladder));
    }

}
