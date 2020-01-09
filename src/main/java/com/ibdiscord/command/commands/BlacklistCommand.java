/* Copyright 2018-2020 Arraying
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
import net.dv8tion.jda.api.Permission;

import java.util.Set;

public final class BlacklistCommand extends Command {

    /**
     * Creates the command.
     */
    public BlacklistCommand() {
        super("blacklist",
                CommandPermission.discord(Permission.BAN_MEMBERS),
                Set.of()
        );
        this.correctUsage = "blacklist <user ID>";
    }

    /**
     * Blacklists a member from the server, by ID.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        if(context.getArguments().length < 1) {
            sendUsage(context);
            return;
        }
        String idRaw = context.getArguments()[0];
        long id;
        try {
            id = Long.valueOf(idRaw);
        } catch(NumberFormatException exception) {
            context.reply(__(context, "error.blacklist_id"));
            return;
        }
        if(context.getGuild().getMemberById(id) != null) {
            context.reply(__(context, "error.blacklist_present"));
            return;
        }
        context.getGuild().ban(idRaw, 0).reason("Blacklisted.").queue(
            success -> context.reply(__(context, "success.blacklist")),
            fail -> context.reply(__(context, "error.blacklist_fail"))
        );
    }

}
