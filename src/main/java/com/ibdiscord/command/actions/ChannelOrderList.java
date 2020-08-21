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
import com.ibdiscord.utils.UString;
import de.arraying.gravity.Gravity;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Category;

import java.util.List;
import java.util.stream.Collectors;

public class ChannelOrderList implements CommandAction {

    /**
     * Returns list of category snapshot.
     * @param context The command context.
     */
    @Override
    public void accept(CommandContext context) {
        context.assertArguments(1, "error.missing_categoryid");
        Category category = UInput.getCategory(context.getGuild(), context.getArguments()[0]);

        if (category == null) {
            context.replyI18n("error.category_invalid");
            return;
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(String.format("Order Of Channels (%s)", category.getName()));
        Gravity gravity = DataContainer.INSTANCE.getGravity();

        ChannelData textChannelData = gravity.load(
                new ChannelData(context.getGuild().getId(), "text")
        );
        List<String> textChannels = UString.listEmpty(textChannelData.get(category.getId()).toString());

        ChannelData voiceChannelData = gravity.load(
                new ChannelData(context.getGuild().getId(), "voice")
        );
        List<String> voiceChannels = UString.listEmpty(voiceChannelData.get(category.getId()).toString());

        if (!textChannels.isEmpty()) {
            embedBuilder.addField("Text Channels", textChannels.stream()
                    .map(id -> context.getGuild().getTextChannelById(id).getName())
                    .collect(Collectors.joining(", ")), false);
        }

        if (!voiceChannels.isEmpty()) {
            embedBuilder.addField("Voice Channels", voiceChannels.stream()
                    .map(id -> context.getGuild().getVoiceChannelById(id).getName())
                    .collect(Collectors.joining(", ")), false);
        }

        context.replyEmbed(embedBuilder.build());
    }
}