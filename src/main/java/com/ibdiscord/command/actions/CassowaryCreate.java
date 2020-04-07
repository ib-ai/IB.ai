/* Copyright 2017-2020 Jarred Vardy <vardy@riseup.net>, Arraying
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

package com.ibdiscord.command.actions;

import com.ibdiscord.command.CommandAction;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.cassowary.CassowariesData;
import com.ibdiscord.data.db.entries.cassowary.CassowaryData;
import com.ibdiscord.utils.UString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CassowaryCreate implements CommandAction {

    /**
     * Create a cassowary.
     * @param context The command context.
     */
    @Override
    public void accept(CommandContext context) {
        context.assertArguments(3, "error.generic_syntax_arg");
        List<String> data = context.assertQuotes(1, "error.generic_syntax_arg");

        String label = data.get(0);

        String allArgs = UString.concat(context.getArguments(), " ", 0);
        String argsWithoutLabel = allArgs.replace(label, "")
                .replaceAll("\"", "")
                .trim();
        ArrayList<String> roleIDs = new ArrayList<>(Arrays.asList(argsWithoutLabel.split(" ")));

        boolean invalidIDs = roleIDs.stream()
                .anyMatch(id -> validateRoleID(id, context));

        if(invalidIDs) {
            context.replyI18n("error.cassowary_id");
        }

        CassowariesData cassowariesData = DataContainer.INSTANCE.getGravity().load(new CassowariesData(
                context.getGuild().getId()
        ));
        cassowariesData.add(label);
        DataContainer.INSTANCE.getGravity().save(cassowariesData);

        CassowaryData cassowaryData = DataContainer.INSTANCE.getGravity().load(new CassowaryData(
                context.getGuild().getId(),
                label
        ));
        roleIDs.forEach(cassowaryData::add);
        DataContainer.INSTANCE.getGravity().save(cassowaryData);

        context.replyI18n("success.done");
    }

    /**
     * Validates the role ID.
     * @param roleID The role ID.
     * @param context The command context.
     * @return True if valid, false otherwise.
     */
    private static boolean validateRoleID(String roleID, CommandContext context) {
        try {
            context.getGuild().getRoleById(roleID);
            return false;
        } catch(Exception ex) {
            return true;
        }
    }

}
