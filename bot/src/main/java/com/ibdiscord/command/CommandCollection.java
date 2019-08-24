package com.ibdiscord.command;

import com.ibdiscord.command.commands.*;
import com.ibdiscord.command.commands.cassowary.CassowaryCommand;
import com.ibdiscord.command.commands.filter.FilterCommand;
import com.ibdiscord.command.commands.monitor.MonitorCommand;
import com.ibdiscord.command.commands.react.ReactionCommand;
import com.ibdiscord.command.commands.reminder.ReminderCommand;
import com.ibdiscord.command.commands.tag.TagCommand;
import com.ibdiscord.command.commands.voting.ladder.VoteLadderCommand;
import com.ibdiscord.command.commands.voting.vote.VoteCommand;
import lombok.Getter;

/**
 * Copyright 2017-2019 Arraying
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
public enum CommandCollection {


    AVATAR(new AvatarCommand()),
    BLACKLIST(new BlacklistCommand()),
    CASSOWARY(new CassowaryCommand()),
    ECHO(new EchoCommand()),
    EMBED(new EmbedCommand()),
    EVAL(new EvalCommand()),
    EXPIRE(new ExpireCommand()),
    FILTER(new FilterCommand()),
    FIND(new FindCommand()),
    GIVE_ROLE(new GiveRoleCommand()),
    HELP(new HelpCommand()),
    LANG(new LangCommand()),
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
    ROLE_SWAP(new RoleSwapCommand()),
    SERVER_INFO(new ServerInfoCommand()),
    TAG(new TagCommand()),
    USER_INFO(new UserInfoCommand()),
    USER_ROLES(new UserRolesCommand()),
    VOTE(new VoteCommand()),
    VOTE_LADDER(new VoteLadderCommand()),
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
