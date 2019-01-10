package com.ibdiscord.command.commands;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.utils.UString;

import java.util.HashSet;
import java.util.Set;

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
public final class EchoCommand extends Command {

    /**
     * Constructs the echo command.
     */
    public EchoCommand() {
        super("echo", Set.of("copycat"), CommandPermission.discord(), new HashSet<>());
    }

    /**
     * Returns what the user inputs.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        if(context.getArguments().length < 2) {
            context.reply("You need to provide a message.");
            return;
        }
        context.reply(UString.stripMassMentions(
                UString.concat(context.getArguments(), " ", 1)
        ));
    }

}
