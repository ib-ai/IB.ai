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
import de.arraying.gravity.Gravity;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ISnowflake;

import java.util.List;
import java.util.stream.Collectors;

public final class ChannelOrderSnapshot implements CommandAction {

    /**
     * Creates snapshot of channel order.
     * @param context The command context.
     */
    @Override
    public void accept(CommandContext context) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Order Of Channels");

        Gravity gravity = DataContainer.INSTANCE.getGravity();

        context.getGuild().getCategories().forEach(category -> {
            ChannelData textChannelData = gravity.load(
                    new ChannelData(context.getGuild().getId(), "text")
            );

            ChannelData voiceChannelData = gravity.load(
                    new ChannelData(context.getGuild().getId(), "voice")
            );

            List<String> textChannels = category.getTextChannels().size() < 1 ? null :
                    category.modifyTextChannelPositions().getCurrentOrder()
                    .stream()
                    .map(ISnowflake::getId)
                    .collect(Collectors.toList());

            List<String> voiceChannels = category.getVoiceChannels().size() < 1 ? null :
                    category.modifyVoiceChannelPositions().getCurrentOrder()
                            .stream()
                            .map(ISnowflake::getId)
                            .collect(Collectors.toList());

            if (textChannels != null) {
                StringBuilder textStrings = new StringBuilder();

                textChannelData.set(category.getId(), textChannels);

                gravity.save(textChannelData);

                textChannels.forEach(guildChannel -> {
                    textStrings.append(context.getGuild().getGuildChannelById(guildChannel).getName()).append(", ");
                });

                embedBuilder.addField(String.format("Text Channels (%s)",
                        category.getName()), textStrings.toString(), false);
            }


            if (voiceChannels != null) {
                StringBuilder voiceStrings = new StringBuilder();

                voiceChannelData.set(category.getId(), voiceChannels);

                gravity.save(voiceChannelData);

                voiceChannels.forEach(guildChannel -> {
                    voiceStrings.append(context.getGuild().getGuildChannelById(guildChannel).getName()).append(", ");
                });

                embedBuilder.addField(String.format("Voice Channels (%s)",
                        category.getName()), voiceStrings.toString(), false);
            }
        });

        context.replyEmbed(embedBuilder.build());
    }
}
