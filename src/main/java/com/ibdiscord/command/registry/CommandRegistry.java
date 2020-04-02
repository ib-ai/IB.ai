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

package com.ibdiscord.command.registry;

import com.ibdiscord.command.Command;
import com.ibdiscord.i18n.LocaliserHandler;
import com.ibdiscord.utils.UCommand;
import com.ibdiscord.utils.objects.Comparator;

import java.util.HashSet;
import java.util.TreeSet;

public final class CommandRegistry {

    private final TreeSet<Command> commands = new TreeSet<>(new Comparator());

    /**
     * Defines a command.
     * This will create the command object, for which all sorts of meta + the action can be defined.
     * @param name The name of the command.
     * @return A command object.
     */
    public Command define(String name) {
        Command command = new Command(name, LocaliserHandler.INSTANCE.getCommandAliases(name));
        commands.add(command);
        return command;
    }

    /**
     * Creates a sub-command with that name.
     * @param name The name.
     * @param aliases The key for all the aliases.
     * @return A command object.
     */
    public Command sub(String name, String aliases) {
        return new Command(name, aliases == null
                ? new HashSet<>()
                : LocaliserHandler.INSTANCE.getCommandAliases(aliases));
    }

    /**
     * Queries a command by name.
     * @param name The command name.
     * @return A command, or null if it could not be found.
     */
    public Command query(String name) {
        return UCommand.query(commands, name);
    }

}
