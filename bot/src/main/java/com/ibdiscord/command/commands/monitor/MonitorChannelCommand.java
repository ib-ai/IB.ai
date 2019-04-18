package com.ibdiscord.command.commands.monitor;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.DContainer;
import com.ibdiscord.data.db.entries.monitor.MonitorData;
import de.arraying.gravity.Gravity;
import net.dv8tion.jda.core.Permission;

import java.util.Set;

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
public final class MonitorChannelCommand extends Command {

    /**
     * Creates the command.
     */
    MonitorChannelCommand() {
        super("channel",
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
        monitorData.set(MonitorData.CHANNEL, context.getMessage().getMentionedChannels().get(0).getId());
        gravity.save(monitorData);
        context.reply("Consider it done.");
    }

}
