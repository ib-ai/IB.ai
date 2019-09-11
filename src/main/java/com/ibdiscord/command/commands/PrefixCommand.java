/* Copyright 2017-2019 Ray Clark
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
import net.dv8tion.jda.api.Permission;

import java.util.Arrays;
import java.util.Set;

public final class PrefixCommand extends Command {

    /**
     * Creates the command.
     */
    public PrefixCommand() {
        super("prefix",
                Set.of("setprefix"),
                CommandPermission.discord(Permission.MANAGE_SERVER),
                Set.of()
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
            context.reply("Invalid Prefix ( " +  prefixNew + ").");
            return;
        }

        GuildData guildData = DataContainer.INSTANCE.getGravity().load(new GuildData(context.getGuild().getId()));
        guildData.set(GuildData.PREFIX, prefixNew);
        DataContainer.INSTANCE.getGravity().save(guildData);
        context.reply("The prefix has been updated to (" + prefixNew + ").");
    }

}
