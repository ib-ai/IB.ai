package com.ibdiscord.command.commands.monitor;

import com.ibdiscord.command.CommandContext;
import com.ibdiscord.data.db.DContainer;
import com.ibdiscord.data.db.entries.monitor.MonitorUserData;
import com.ibdiscord.utils.UInput;
import de.arraying.gravity.Gravity;
import de.arraying.gravity.data.property.Property;

import java.util.List;
import java.util.stream.Collectors;

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
public final class MonitorUserCommand extends MonitorManageCommand {

    /**
     * Creates the command.
     */
    MonitorUserCommand() {
        super("user");
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
        Gravity gravity = DContainer.INSTANCE.getGravity();
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
        Gravity gravity = DContainer.INSTANCE.getGravity();
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
        return DContainer.INSTANCE.getGravity().load(new MonitorUserData(context.getGuild().getId())).values().stream()
                .map(Property::asString)
                .collect(Collectors.toList());
    }

}
