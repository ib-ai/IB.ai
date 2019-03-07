package com.ibdiscord.command.commands;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.DContainer;
import com.ibdiscord.data.db.entries.GuildData;
import com.ibdiscord.utils.UString;
import de.arraying.gravity.Gravity;
import net.dv8tion.jda.core.Permission;

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
public final class ModeratorCommand extends Command {

    /**
     * Creates the command.
     */
    public ModeratorCommand() {
        super("moderator",
                Set.of("mod"),
                CommandPermission.discord(Permission.MANAGE_SERVER),
                new HashSet<>()
        );
        this.correctUsage = "mod [new role]";
    }

    /**
     * Sets the moderator permission.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        Gravity gravity = DContainer.INSTANCE.getGravity();
        GuildData guildData = gravity.load(new GuildData(context.getGuild().getId()));
        if(context.getArguments().length == 0) {
            String permission = guildData.get(GuildData.MODERATOR)
                    .defaulting("not set")
                    .asString();
            context.reply("The moderator permission is currently: " + permission + ".");
            return;
        }
        String newValue = UString.concat(context.getArguments(), " ", 0);
        guildData.set(GuildData.MODERATOR, newValue);
        gravity.save(guildData);
        context.reply("The moderator permission has been updated.");
    }

}
