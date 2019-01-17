package com.ibdiscord.command.commands;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.utils.UInput;
import net.dv8tion.jda.core.entities.Role;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Copyright 2018 Arraying
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
public final class UserRolesCommand extends Command {

    /**
     * Executes the command.
     */
    public UserRolesCommand() {
        super("userroles", Set.of("myroles", "showroles"), CommandPermission.discord(), new HashSet<>());
    }

    /**
     * Shows a list of user roles.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        var target = context.getMember();
        if(context.getArguments().length >= 1) {
            target = UInput.getMember(context.getGuild(), context.getArguments()[0]);
        }
        if(target == null) {
            context.reply("User not found!");
            return;
        }
        context.reply("This user has the following roles: `%s`.", target.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.joining(", "))
        );
    }

}
