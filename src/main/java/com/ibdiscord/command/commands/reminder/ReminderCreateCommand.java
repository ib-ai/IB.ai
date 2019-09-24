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

package com.ibdiscord.command.commands.reminder;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.reminder.ReminderHandler;
import com.ibdiscord.utils.UString;
import com.ibdiscord.utils.UTime;

import java.util.Set;

public final class ReminderCreateCommand extends Command {

    /**
     * Creates the command.
     */
    ReminderCreateCommand() {
        super("reminder_create",
                CommandPermission.discord(),
                Set.of()
        );
    }

    /**
     * Creates a reminder.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        if(context.getArguments().length < 1) {
            context.reply(__(context, "error.reminder_duration"));
            return;
        }
        if(context.getArguments().length < 2) {
            context.reply(__(context, "error.reminder_content"));
            return;
        }
        long duration = UTime.parseDuration(context.getArguments()[0]);
        if(duration == -1) {
            context.reply(__(context, "error.reminder_format"));
            return;
        }
        String reminder = UString.concat(context.getArguments(), " ", 1);
        ReminderHandler.INSTANCE.create(context.getMember().getUser(), duration, reminder);
        context.reply(__(context, "success.reminder_schedule"));
    }
}
