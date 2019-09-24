/* Copyright 2017-2019 Jarred Vardy
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

package com.ibdiscord.command.commands.cassowary;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.cassowary.CassowariesData;
import com.ibdiscord.data.db.entries.cassowary.CassowaryData;
import com.ibdiscord.utils.UInput;
import net.dv8tion.jda.api.Permission;

import java.util.List;
import java.util.Set;

public final class CassowaryDeleteCommand extends Command {

    /**
     * Creates a new CassowaryDeleteCommand command.
     */
    CassowaryDeleteCommand() {
        super("cassowary_delete",
                CommandPermission.discord(Permission.MANAGE_SERVER),
                Set.of()
        );
        this.correctUsage = "cassowary delete \"label\"";
    }

    @Override
    protected void execute(CommandContext context) {
        if(context.getArguments().length < 1) {
            sendUsage(context);
            return;
        }
        List<String> quotedStrings = UInput.extractQuotedStrings(context.getArguments());
        if(quotedStrings.isEmpty()) {
            sendUsage(context);
            return;
        }
        String label = quotedStrings.get(0);

        CassowariesData cassowariesData = DataContainer.INSTANCE.getGravity().load(new CassowariesData());
        cassowariesData.remove(label);
        DataContainer.INSTANCE.getGravity().save(cassowariesData);

        DataContainer.INSTANCE.getGravity().load(new CassowaryData(label)).delete();

        context.reply(__(context, "success.done"));
    }
}