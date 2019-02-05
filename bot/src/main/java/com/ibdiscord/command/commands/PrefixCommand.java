package com.ibdiscord.command.commands;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.DContainer;
import com.ibdiscord.data.db.entries.GuildData;
import net.dv8tion.jda.core.Permission;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Copyright 2019 Ray Clark
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
