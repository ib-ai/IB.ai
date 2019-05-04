/**
 * Copyright 2017-2019 Arraying
 *
 * This file is part of LoyalBot.
 *
 * LoyalBot is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LoyalBot is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LoyalBot. If not, see http://www.gnu.org/licenses/.
 */

package dev.vardy.command.commands;

import dev.vardy.command.Command;
import dev.vardy.command.CommandContext;
import dev.vardy.command.permissions.CommandPermission;
import dev.vardy.data.db.DContainer;
import dev.vardy.data.db.entries.GuildData;

import de.arraying.gravity.Gravity;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Role;

import java.util.HashSet;
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
