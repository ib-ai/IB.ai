/* Copyright 2020 Nathaneal Varghese
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
import com.ibdiscord.data.db.entries.ChannelData;
import com.ibdiscord.utils.UInput;
import com.ibdiscord.utils.UPermission;
import de.arraying.gravity.Gravity;
import net.dv8tion.jda.api.entities.*;

public final class ChannelOrderRollback implements CommandAction {

    /**
     * Rolls back channel positions.
     *
     * @param context The command context.
     */
    @Override
    public void accept(CommandContext context) {
        Gravity gravity = DataContainer.INSTANCE.getGravity();
        Member selfMember = context.getGuild().getSelfMember();

        ChannelData textChannelData = gravity.load(new ChannelData(context.getGuild().getId(), "text"));
        ChannelData voiceChannelData = gravity.load(new ChannelData(context.getGuild().getId(), "voice"));

        textChannelData.getKeys().forEach(categoryId -> {
            Category category = context.getGuild().getCategoryById(categoryId);
            String[] textChannels = textChannelData.get(categoryId).toString()
                    .split(",");

            for (int i = 0; i < textChannels.length; i++) {
                TextChannel channel = UInput.getChannel(context.getGuild(), textChannels[i]);
                if (channel != null && UPermission.canMoveChannel(selfMember, channel)) {
                    channel.getManager().setParent(category).setPosition(i).queue();
                }
            }
        });

        voiceChannelData.getKeys().forEach(categoryId -> {
            Category category = context.getGuild().getCategoryById(categoryId);
            String[] voiceChannels = voiceChannelData.get(categoryId).toString()
                    .split(",");

            for (int i = 0; i < voiceChannels.length; i++) {
                VoiceChannel channel = (VoiceChannel) UInput.getChannelGuild(context.getGuild(), voiceChannels[i], true);
                if (channel != null && UPermission.canMoveChannel(selfMember, channel)) {
                    channel.getManager().setParent(category).setPosition(i).queue();
                }
            }
        });

        context.replyI18n("success.done");
    }
}

