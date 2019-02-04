package com.ibdiscord.command;

import com.ibdiscord.command.commands.*;
import com.ibdiscord.command.commands.tag.TagCommand;
import lombok.Getter;

/**
 * Copyright 2019 Arraying
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public enum CommandCollection {

    ECHO(new EchoCommand()),
    EVAL(new EvalCommand()),
    EXPIRE(new ExpireCommand()),
    HELP(new HelpCommand()),
    MOD_LOG(new ModLogCommand()),
    MUTE_ROLE(new MuteRoleCommand()),
    PING(new PingCommand()),
    REASON(new ReasonCommand()),
    SERVER_INFO(new ServerInfoCommand()),
    SET_PREFIX(new PrefixCommand()),
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
