/**
 * Copyright 2017-2019 Ray Clark
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
import net.dv8tion.jda.core.Permission;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class PrefixCommand extends Command {

    /**
     * Creates the command.
     */
    public PrefixCommand() {
        super("prefix",
                Set.of("setprefix"),
                CommandPermission.discord(Permission.MANAGE_SERVER),
                new HashSet<>()
        );
        this.correctUsage = "prefix <new prefix>";
    }

    /**
     * Sets the prefix of the bot, per guild.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        if(context.getArguments().length == 0) {
            sendUsage(context);
            return;
        }

        String prefixNew = context.getArguments()[0];
        if(Arrays.stream(new String[]{"/", "$", "#", "+", "*", "?"}).anyMatch(prefixNew::equals)) {
            context.reply("Invalid Prefix ( " +  prefixNew + ")." );
            return;
        }

        GuildData guildData = DContainer.INSTANCE.getGravity().load(new GuildData(context.getGuild().getId()));
        guildData.set(GuildData.PREFIX, prefixNew);
        DContainer.INSTANCE.getGravity().save(guildData);
        context.reply("The prefix has been updated to (" + prefixNew + ").");
    }

}
