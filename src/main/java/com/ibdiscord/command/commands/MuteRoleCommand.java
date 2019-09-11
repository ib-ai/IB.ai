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

package com.ibdiscord.command.commands;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.GuildData;
import de.arraying.gravity.Gravity;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;

import java.util.List;
import java.util.Set;

public final class MuteRoleCommand extends Command {

    /**
     * Creates the command.
     */
    public MuteRoleCommand() {
        super("muterole",
                Set.of("setmuterole"),
                CommandPermission.discord(Permission.MANAGE_ROLES),
                Set.of()
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
            context.reply("Please mention the role that you would like to act as a muted role. It is "
                    + "recommended to do this in a private channel.");
            return;
        }
        Gravity gravity = DataContainer.INSTANCE.getGravity();
        GuildData guildData = gravity.load(new GuildData(context.getGuild().getId()));
        guildData.set(GuildData.MUTE, roles.get(0).getId());
        gravity.save(guildData);
        context.reply("The new role has been set.");
    }

}
