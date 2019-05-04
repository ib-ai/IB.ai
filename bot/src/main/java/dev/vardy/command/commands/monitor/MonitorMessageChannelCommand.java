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

package dev.vardy.command.commands.monitor;

import dev.vardy.command.Command;
import dev.vardy.command.CommandContext;
import dev.vardy.command.permissions.CommandPermission;
import dev.vardy.data.db.DContainer;
import dev.vardy.data.db.entries.monitor.MonitorData;
import de.arraying.gravity.Gravity;
import net.dv8tion.jda.core.Permission;

import java.util.Set;

public class MonitorMessageChannelCommand extends Command {

    /**
     * Creates the command.
     */
    MonitorMessageChannelCommand() {
        super("messagechannel",
                Set.of(),
                CommandPermission.discord(Permission.MANAGE_SERVER),
                Set.of()
        );
    }

    /**
     * Sets the channel.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        if(context.getMessage().getMentionedChannels().isEmpty()) {
            context.reply("Please mention a channel!");
            return;
        }
        Gravity gravity = DContainer.INSTANCE.getGravity();
        MonitorData monitorData = gravity.load(new MonitorData(context.getGuild().getId()));
        monitorData.set(MonitorData.MESSAGE_CHANNEL, context.getMessage().getMentionedChannels().get(0).getId());
        gravity.save(monitorData);
        context.reply("Consider it done.");
    }

}
