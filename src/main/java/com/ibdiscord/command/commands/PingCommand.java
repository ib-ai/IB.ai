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

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import net.dv8tion.jda.api.JDA;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class PingCommand extends Command {

    /**
     * Creates the command.
     */
    public PingCommand() {
        super("ping",
                Stream.of("pong", "latency").collect(Collectors.toSet()),
                CommandPermission.discord(),
                Set.of()
        );
    }

    /**
     * Returns the current shard's WSS ping.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        JDA jda = context.getGuild().getJDA();
        context.reply("Pong! WebSocket latency is currently %d ms and %d ms for REST and WSS respectively.",
                jda.getRestPing().complete(),
                jda.getGatewayPing()
        );
    }

}
