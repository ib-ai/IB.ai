/* Copyright 2018-2020 Arraying
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

package com.ibdiscord.command.commands.monitor;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.monitor.MonitorData;
import de.arraying.gravity.Gravity;
import net.dv8tion.jda.api.Permission;

import java.util.Set;

public final class MonitorToggleCommand extends Command {

    /**
     * Creates the command.
     */
    MonitorToggleCommand() {
        super("monitor_toggle",
                CommandPermission.discord(Permission.MANAGE_SERVER),
                Set.of()
        );
    }

    /**
     * Toggles monitoring.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        Gravity gravity = DataContainer.INSTANCE.getGravity();
        MonitorData monitorData = gravity.load(new MonitorData(context.getGuild().getId()));
        boolean current = monitorData.get(MonitorData.ENABLED)
                .defaulting(false)
                .asBoolean();
        monitorData.set(MonitorData.ENABLED, !current);
        gravity.save(monitorData);
        context.reply(current ? __(context, "success.monitor_disable") : __(context, "success.monitor_enable"));
    }
}
