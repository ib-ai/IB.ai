package com.ibdiscord.command.commands;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.CommandPermission;

import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
public final class PingCommand extends Command {

    public PingCommand() {
        super("ping",
                Stream.of("pong", "latency").collect(Collectors.toSet()),
                CommandPermission.discord(),
                new HashSet<>()
        );
    }

    @Override
    protected void execute(CommandContext context) {
        context.reply("Pong! WebSocket latency is currently %d.", context.getGuild().getJDA().getPing());
    }

}
