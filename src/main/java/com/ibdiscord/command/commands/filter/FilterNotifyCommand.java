/* Copyright 2019 Arraying
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

package com.ibdiscord.command.commands.filter;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.filter.FilterNotifyData;
import com.ibdiscord.utils.UInput;
import net.dv8tion.jda.api.Permission;

import java.util.List;
import java.util.Set;

public final class FilterNotifyCommand extends Command {

    /**
     * Creates the command.
     */
    FilterNotifyCommand() {
        super("filter_notify",
                CommandPermission.discord(Permission.MANAGE_SERVER),
                Set.of()
        );
        this.correctUsage = "filter notify \"trigger\"";
    }

    /**
     * Toggles a certain tag as active.
     * Deactivated tags still exist, but cannot be triggered. Useful for temporary tags.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        if(context.getArguments().length == 0) {
            sendUsage(context);
            return;
        }
        List<String> data = UInput.extractQuotedStrings(context.getArguments());
        if(data.size() < 1) {
            context.reply(__(context, "error.missing_data"));
            return;
        }
        String trigger = data.get(0);
        if(!UInput.isValidRegex(trigger)) {
            context.reply(__(context, "error.filter"));
            return;
        }
        FilterNotifyData filterNotifyData = DataContainer.INSTANCE.getGravity()
                .load(new FilterNotifyData(context.getGuild().getId()));
        if(filterNotifyData.contains(trigger)) {
            filterNotifyData.remove(trigger);
            context.reply(__(context, "info.filter_enabled"));
        } else {
            filterNotifyData.add(trigger);
            context.reply(__(context, "info.filter_disabled"));
        }
        DataContainer.INSTANCE.getGravity().save(filterNotifyData);
    }

}
