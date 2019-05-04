/**
 * Copyright 2017-2019 Arraying
 *
 * This file is part of LoyalBot.
 *
 * LoyalBot is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LoyalBot is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LoyalBot. If not, see http://www.gnu.org/licenses/.
 */

package dev.vardy.command.commands;

import dev.vardy.command.commands.abstracted.LoggingCommand;
import dev.vardy.command.permissions.CommandPermission;
import dev.vardy.data.db.entries.GuildData;

import net.dv8tion.jda.core.Permission;

import java.util.HashSet;
import java.util.Set;

public final class ModLogCommand extends LoggingCommand {

    /**
     * Creates the command.
     */
    public ModLogCommand() {
        super("modlog",
                Set.of("setmodlog"),
                CommandPermission.discord(Permission.MANAGE_SERVER),
                new HashSet<>(),
                GuildData.MODLOGS
        );
        this.correctUsage = "modlog [new channel]";
    }

}
