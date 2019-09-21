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

package com.ibdiscord.command.commands.monitor;

import com.ibdiscord.command.CommandContext;
import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.monitor.MonitorUserData;
import com.ibdiscord.utils.UInput;
import de.arraying.gravity.Gravity;
import de.arraying.gravity.data.property.Property;

import java.util.List;
import java.util.stream.Collectors;

public final class MonitorUserCommand extends MonitorManageCommand {

    /**
     * Creates the command.
     */
    MonitorUserCommand() {
        super("monitor_user");
    }

    /**
     * Checks if the input is a user.
     * @param context The command context.
     * @param input The input.
     * @return True if it is, false otherwise.
     */
    @Override
    protected boolean isValidInput(CommandContext context, String input) {
        return UInput.getMember(context.getGuild(), input) != null;
    }

    /**
     * Adds the user.
     * @param context The command context.
     * @param input The input.
     */
    @Override
    protected void add(CommandContext context, String input) {
        Gravity gravity = DataContainer.INSTANCE.getGravity();
        MonitorUserData userData = gravity.load(new MonitorUserData(context.getGuild().getId()));
        //noinspection ConstantConditions
        userData.add(UInput.getMember(context.getGuild(), input).getUser().getId());
        gravity.save(userData);
    }

    /**
     * Removes the user.
     * @param context The command context.
     * @param input The input.
     */
    @Override
    protected void remove(CommandContext context, String input) {
        Gravity gravity = DataContainer.INSTANCE.getGravity();
        MonitorUserData userData = gravity.load(new MonitorUserData(context.getGuild().getId()));
        //noinspection ConstantConditions
        userData.remove(UInput.getMember(context.getGuild(), input).getUser().getId());
        gravity.save(userData);
    }

    /**
     * Gets a list of users.
     * @param context The command context.
     * @return A list of user IDs.
     */
    @Override
    protected List<String> list(CommandContext context) {
        return DataContainer.INSTANCE.getGravity().load(new MonitorUserData(context.getGuild().getId())).values()
                .stream()
                .map(Property::asString)
                .collect(Collectors.toList());
    }

}
