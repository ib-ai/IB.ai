/**
 * Copyright 2018 raynichc
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
 * @author raynichc
 * @since 2018.11.29
 */

package com.ibdiscord.command.commands;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.DContainer;
import com.ibdiscord.data.db.entries.GuildData;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.HashSet;

public final class ModLogCommand extends Command {

    /**
     * The ID of a disabled modlog.
     */
    public static final long DISABLED_MOD_LOG = 0L;

    public ModLogCommand() {
        super("setmodlog",
                new HashSet<>(),
                CommandPermission.discord(Permission.MANAGE_SERVER),
                new HashSet<>());
    }
    @Override
    protected void execute(CommandContext context) {
        if(context.getMessage().getMentionedChannels().isEmpty()) {
            context.reply("Please mention the channel to use.");
            return;
        }
        TextChannel channel = context.getMessage().getMentionedChannels().get(0);
        GuildData guildData = DContainer.INSTANCE.getGravity().load(new GuildData(context.getGuild().getId()));
        guildData.set(GuildData.MODLOGS, channel.getId());
        DContainer.INSTANCE.getGravity().save(guildData);
        context.reply("The channel has been set to: " + channel.getAsMention() + ".");
//        String botPrefix = DContainer.getGravity().load(new BotPrefixData(context.getGuild().getId())).get().defaulting(IBai.getConfig().getStaticPrefix()).toString();
//
//        if (context.getArguments().length != 1) {
//            context.reply("Correct usage: `" + botPrefix + "SetModLog [ModLog Channel ID]`");
//            return;
//        }
//
//        String channelID = context.getArguments()[0];
//
//        if(context.getGuild().getTextChannelById(channelID) == null) {
//            context.reply("Invalid Channel ID ( " +  channelID + ")." );
//            return;
//        }
//
//        try {
//            ModLogData modLogID = new ModLogData(context.getGuild().getId());
//            modLogID.set(channelID);
//            DContainer.getGravity().save(modLogID);
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
    }
}
