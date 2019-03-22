package com.ibdiscord.command.commands.reminder;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.reminder.ReminderHandler;
import com.ibdiscord.utils.UString;
import com.ibdiscord.utils.UTime;

import java.util.HashSet;
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
public final class ReminderCreateCommand extends Command {

    /**
     * Creates the command.
     */
    ReminderCreateCommand() {
        super("create",
                Set.of("c", "add", "new"),
                CommandPermission.discord(),
                new HashSet<>()
        );
    }

    /**
     * Creates a reminder.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        if(context.getArguments().length < 1) {
            context.reply("Please provide the duration until you want to be reminded.");
            return;
        }
        if(context.getArguments().length < 2) {
            context.reply("Please provide the reminder.");
            return;
        }
        long duration = UTime.parseDuration(context.getArguments()[0]);
        if(duration == -1) {
            context.reply("Your duration is invalid. For example, a reminder in 3 hours and 15 minutes would be `3h15`.");
            return;
        }
        String reminder = UString.concat(context.getArguments(), " ", 1);
        ReminderHandler.INSTANCE.create(context.getMember().getUser(), duration, reminder);
        context.reply("The reminder has been scheduled!");
    }

}
