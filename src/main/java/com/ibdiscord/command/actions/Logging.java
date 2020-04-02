/* Copyright 2017-2020 Arraying
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

package com.ibdiscord.command.actions;

import com.ibdiscord.command.CommandAction;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.GuildData;
import net.dv8tion.jda.api.entities.TextChannel;

public final class Logging implements CommandAction {

    /**
     * A disabled log.
     */
    private static final long DISABLED = 0L;

    private final String key;

    /**
     * Creates a new command.
     * @param key The key for the channel field.
     */
    public Logging(String key) {
        this.key = key;
    }

    /**
     * Sets or disables the logging channel.
     * @param context The command context.
     */
    @Override
    public void accept(CommandContext context) {
        TextChannel channel;
        if(context.getMessage().getMentionedChannels().isEmpty()) {
            channel = null;
        } else {
            channel = context.getMessage().getMentionedChannels().get(0);
        }
        GuildData guildData = DataContainer.INSTANCE.getGravity().load(new GuildData(context.getGuild().getId()));
        guildData.set(key, channel == null ? DISABLED : channel.getId());
        DataContainer.INSTANCE.getGravity().save(guildData);
        context.replyI18n("success.logging", channel == null ? "N/A" : channel.getAsMention());
    }

}
