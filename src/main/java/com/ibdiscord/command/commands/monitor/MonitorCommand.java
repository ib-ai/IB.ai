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

package com.ibdiscord.command.commands.monitor;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.entries.GuildData;

import java.util.Set;

public final class MonitorCommand extends Command {

    /**
     * Creates the command.
     */
    public MonitorCommand() {
        super("monitor",
                Set.of("nsa"),
                CommandPermission.role(GuildData.MODERATOR),
                Set.of(new MonitorToggleCommand(),
                        new MonitorUserChannelCommand(),
                        new MonitorUserCommand(),
                        new MonitorMessageChannelCommand(),
                        new MonitorMessageCommand(),
                        new MonitorListCommand()
                )
        );
        this.correctUsage = "monitor <toggle|userchannel|messagechannel|user|message|list> "
                + "[channel]|create|delete [value]";
    }

    /**
     * Sends the usage.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        sendUsage(context);
    }

}
