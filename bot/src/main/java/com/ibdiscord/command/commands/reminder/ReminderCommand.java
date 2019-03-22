package com.ibdiscord.command.commands.reminder;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;

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
public final class ReminderCommand extends Command {

    /**
     * Creates the command.
     */
    public ReminderCommand() {
        super("reminder",
                Set.of("remindme", "remind", "yeet"),
                CommandPermission.discord(),
                Set.of(new ReminderCreateCommand(),
                        new ReminderDeleteCommand(),
                        new ReminderListCommand()
                )
        );
        this.correctUsage = "reminder <create/delete/list> <text/id/page number>";
    }

    /**
     * Sends the usage message.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        sendUsage(context);
    }

}
