/* Copyright 2017-2020 Jarred Vardy <vardy@riseup.net>
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
import com.ibdiscord.data.db.entries.monitor.MonitorUserData;
import de.arraying.gravity.data.property.Property;
import net.dv8tion.jda.api.entities.Member;

import java.util.List;
import java.util.stream.Collectors;

public final class MonitorCleanup implements CommandAction {

    /**
     * Cleans up monitored users who are no longer in a server.
     * @param context The command context.
     */
    @Override
    public void accept(CommandContext context) {
        MonitorUserData monitorUserData = new MonitorUserData(context.getGuild().getId());
        List<String> monitoredUsers = DataContainer.INSTANCE.getGravity().load(monitorUserData)
                .values()
                .stream()
                .map(Property::asString)
                .collect(Collectors.toList());

        List<String> guildMemberIDs = context.getGuild().getMembers()
                .stream()
                .map(Member::getId)
                .collect(Collectors.toList());

        // Find all users in monitor but not in guild member list.
        monitoredUsers.removeAll(guildMemberIDs);

        for(String userToClean : monitoredUsers) {
            monitorUserData.remove(userToClean);
        }

        DataContainer.INSTANCE.getGravity().save(monitorUserData);

        context.replyI18n("success.done");
    }

}
