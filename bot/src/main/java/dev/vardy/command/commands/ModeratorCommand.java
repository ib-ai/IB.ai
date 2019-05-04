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
import dev.vardy.utils.UString;

import de.arraying.gravity.Gravity;

import net.dv8tion.jda.core.Permission;

import java.util.HashSet;
import java.util.Set;

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
