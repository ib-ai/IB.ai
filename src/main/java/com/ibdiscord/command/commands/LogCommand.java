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

package com.ibdiscord.command.commands;

import com.ibdiscord.command.commands.abstracted.LoggingCommand;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.entries.GuildData;
import net.dv8tion.jda.api.Permission;

import java.util.Set;

public final class LogCommand extends LoggingCommand {

    /**
     * Creates the command.
     */
    public LogCommand() {
        super("log",
                Set.of("setlog"),
                CommandPermission.discord(Permission.MANAGE_SERVER),
                Set.of(),
                GuildData.LOGS
        );
        this.correctUsage = "log [new channel]";
    }

}
