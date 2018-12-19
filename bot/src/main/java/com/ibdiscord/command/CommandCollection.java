/**
 * Copyright 2018 Arraying
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

/**
 * @author Arraying
 * @since 2018.09.17
 */

package com.ibdiscord.command;

import com.ibdiscord.command.commands.*;

import lombok.Getter;

public enum CommandCollection {

    // Commands listed as enumerations.
    // COMMAND_NAME(new NameCommand());
    PING(new PingCommand()),
    TEST_OPTIONS(new TestOptionsCommand()),
    TAG(new TagCommand()),
    HELP(new HelpCommand()),
    MOD_LOG(new ModLogCommand()),
    KICK(new KickCommand()),
    SET_PREFIX(new SetPrefixCommand());

    @Getter private final Command command;

    CommandCollection(Command command) {
        this.command = command;
    }
}
