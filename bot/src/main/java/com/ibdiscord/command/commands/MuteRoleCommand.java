package com.ibdiscord.command.commands;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.DContainer;
import com.ibdiscord.data.db.entries.GuildData;
import de.arraying.gravity.Gravity;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Role;

import java.util.HashSet;
import java.util.List;
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
public final class MuteRoleCommand extends Command {

    /**
     * Creates the command.
     */
    public MuteRoleCommand() {
        super("muterole",
                Set.of("setmuterole"),
                CommandPermission.discord(Permission.MANAGE_ROLES),
                new HashSet<>()
        );
        this.correctUsage = "muterole <role>";
    }

    /**
     * Sets the mute role.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        List<Role> roles = context.getMessage().getMentionedRoles();
        if(roles.isEmpty()) {
            context.reply("Please mention the role that you would like to act as a muted role. It is recommended to do this in a private channel.");
            return;
        }
        Gravity gravity = DContainer.INSTANCE.getGravity();
        GuildData guildData = gravity.load(new GuildData(context.getGuild().getId()));
        guildData.set(GuildData.MUTE, roles.get(0).getId());
        gravity.save(guildData);
        context.reply("The new role has been set.");
    }

}
