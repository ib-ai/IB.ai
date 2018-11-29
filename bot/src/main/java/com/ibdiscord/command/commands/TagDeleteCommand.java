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

/**
 * @author Arraying
 * @since 2018.09.17
 */

package com.ibdiscord.command.commands;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.main.IBai;
import net.dv8tion.jda.core.Permission;

import java.util.Arrays;
import java.util.HashSet;

public final class TagDeleteCommand extends Command {

    public TagDeleteCommand() {
        super("delete",
                new HashSet<String>(),
                CommandPermission.discord(Permission.MANAGE_SERVER),
                new HashSet<Command>());
    }

    @Override
    protected void execute(CommandContext context) {

        if (context.getArguments().length == 0) {
            context.reply("Correct usage: `" + IBai.getConfig().getStaticPrefix() + "tag delete \"[trigger]\"`");
        } else {
            StringBuilder triggerBuilder = new StringBuilder();
            for(String message : context.getArguments()) {
                triggerBuilder.append(message);
            }
            String trigger = triggerBuilder.toString().split("\"")[1];
            context.reply("Are you sure you wanna delete the trigger: " + trigger);
        }

    }
}
