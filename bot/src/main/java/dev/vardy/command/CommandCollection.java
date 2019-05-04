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

package dev.vardy.command;

import dev.vardy.command.commands.*;
import dev.vardy.command.commands.monitor.MonitorCommand;
import dev.vardy.command.commands.react.ReactionCommand;
import dev.vardy.command.commands.reminder.ReminderCommand;
import dev.vardy.command.commands.tag.TagCommand;

import lombok.Getter;

public enum CommandCollection {

    ECHO(new EchoCommand()),
    EVAL(new EvalCommand()),
    EXPIRE(new ExpireCommand()),
    HELP(new HelpCommand()),
    LOG(new LogCommand()),
    LOOKUP(new LookupCommand()),
    MODERATOR(new ModeratorCommand()),
    MOD_LOG(new ModLogCommand()),
    MONITOR(new MonitorCommand()),
    MUTE_ROLE(new MuteRoleCommand()),
    NOTES(new NoteCommand()),
    PING(new PingCommand()),
    PREFIX(new PrefixCommand()),
    PURGE(new PurgeCommand()),
    REACTION(new ReactionCommand()),
    REASON(new ReasonCommand()),
    REMINDER(new ReminderCommand()),
    SERVER_INFO(new ServerInfoCommand()),
    TAG(new TagCommand()),
    USER_INFO(new UserInfoCommand()),
    USER_ROLES(new UserRolesCommand()),
    WARN(new WarnCommand());

    @Getter private final Command command;

    /**
     * Registers a new command.
     * @param command The command object.
     */
    CommandCollection(Command command) {
        this.command = command;
    }

}
