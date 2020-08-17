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

        ChannelData textChannelData = gravity.load(new ChannelData(context.getGuild().getId(), "text"));
        ChannelData voiceChannelData = gravity.load(new ChannelData(context.getGuild().getId(), "voice"));

        if (context.getArguments().length > 0) {
            String identifier = context.getArguments()[0];
            String textChannels = textChannelData.get(identifier).toString();
            String voiceChannels = voiceChannelData.get(identifier).toString();

            if (textChannels != null) {
                reorder(context, identifier, textChannels);
            }
            if (voiceChannels != null) {
                reorder(context, identifier, voiceChannels);
            }
        } else {
            textChannelData.getKeys().forEach(categoryId -> {
                reorder(context, categoryId, textChannelData.get(categoryId).toString());
            });

            voiceChannelData.getKeys().forEach(categoryId -> {
                reorder(context, categoryId, voiceChannelData.get(categoryId).toString());
            });
        }

        context.replyI18n("success.done");
    }

    /**
     * Private function to handle reorder.
     * @param context The command context.
     * @param identifier The category identifier.
     * @param channelList List of channels as string.
     */
    private void reorder(CommandContext context, String identifier, String channelList) {
        Category category = UInput.getCategory(context.getGuild(), identifier);
        Member selfMember = context.getGuild().getSelfMember();
        String[] channels = channelList.split(",");

        if (category == null) {
            context.replyI18n("error.category_invalid");
            return;
        }

        for (int i = 0; i < channels.length; i++) {
            GuildChannel channel = UInput.getChannelGuild(context.getGuild(), channels[i], false);
            if (channel != null && UPermission.canMoveChannel(selfMember, channel)) {
                channel.getManager().setParent(category).setPosition(i).queue();
            }
        }
    }
}