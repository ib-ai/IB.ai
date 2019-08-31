package com.ibdiscord.command.commands.cassowary;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.cassowary.CassowariesData;
import com.ibdiscord.data.db.entries.cassowary.CassowaryData;
import com.ibdiscord.utils.UInput;
import com.ibdiscord.utils.UString;
import net.dv8tion.jda.api.Permission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Copyright 2017-2019 Jarred Vardy
 * <p>
 * This file is part of IB.ai.
 * <p>
 * IB.ai is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * IB.ai is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with IB.ai. If not, see http://www.gnu.org/licenses/.
 */

public final class CassowaryCreate extends Command {

    /**
     * Creates a new CassowaryCreate command.
     */
    CassowaryCreate() {
        super("create",
                Set.of("c", "add", "new"),
                CommandPermission.discord(Permission.MANAGE_SERVER),
                Set.of()
        );
    }

    @Override
    protected void execute(CommandContext context) {
        if(context.getArguments().length < 3) {
            sendUsage(context);
            return;
        }
        List<String> quotedStrings = UInput.extractQuotedStrings(context.getArguments());
        if(quotedStrings.isEmpty()) {
            sendUsage(context);
            return;
        }
        String label = quotedStrings.get(0);

        String allArgs = UString.concat(context.getArguments(), " ", 0);
        String argsWithoutLabel = allArgs.replace(label, "")
                .replaceAll("\"", "")
                .trim();
        ArrayList<String> roleIDs = new ArrayList<>(Arrays.asList(argsWithoutLabel.split(" ")));

        boolean invalidIDs = roleIDs.stream()
                .anyMatch(id -> validateRoleID(id, context));

        if(invalidIDs) {
            context.reply("One or more of the role IDs you entered was invalid.");
            return;
        }

        CassowariesData cassowariesData = DataContainer.INSTANCE.getGravity().load(new CassowariesData());
        cassowariesData.add(label);
        DataContainer.INSTANCE.getGravity().save(cassowariesData);

        CassowaryData cassowaryData = DataContainer.INSTANCE.getGravity().load(new CassowaryData(label));
        roleIDs.forEach(cassowaryData::add);
        DataContainer.INSTANCE.getGravity().save(cassowaryData);

        context.reply("Consider it done.");
    }

    private static boolean validateRoleID(String roleID, CommandContext context) {
        try {
            context.getGuild().getRoleById(roleID);
            return false;
        } catch(Exception ex) {
            return true;
        }
    }
}
