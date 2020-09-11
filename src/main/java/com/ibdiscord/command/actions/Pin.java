/* Copyright 2018-2020 Jarred Vardy <vardy@riseup.net>
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

import com.ibdiscord.IBai;
import com.ibdiscord.command.CommandAction;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.utils.objects.Tuple;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;
import java.util.stream.Collectors;

public final class Pin implements CommandAction {

    private final List<Long> subjectChannels;

    /**
     * Populates the subject channels.
     */
    public Pin() {
        subjectChannels = getSubjectChannels();
    }

    /**
     * Pins a message in its channel.
     * @param context The command's context.
     */
    @Override
    public void accept(CommandContext context) {
        context.assertArguments(1, "error.generic_arg_length"); // _At least_ one arg.
        if(context.getArguments().length == 2) { // Channel is specified by user
            TextChannel channel = context.assertChannel(context.getArguments()[0], "error.reaction_channel");
            String msgID = context.getArguments()[1];
            context.assertID(msgID, "error.reaction_message");
            if(!subjectChannels.contains(channel.getIdLong())) {
                context.replyI18n("error.subject_channel");
                return;
            }
            context.getChannel().retrievePinnedMessages().queue(pins -> {
                if (pins.size() == 50) {
                    context.replyI18n("error.pin_max");
                } else {
                    togglePin(context, channel, context.getArguments()[1]);
                    context.replyI18n("success.done");
                }
            });
        } else { // Channel is unspecified. Uses channel the user issued the command from.
            context.assertID(context.getArguments()[0], "error.pin_channel");
            if(!subjectChannels.contains(context.getChannel().getIdLong())) {
                context.replyI18n("error.subject_channel");
                return;
            }
            context.getChannel().retrievePinnedMessages().queue(pins -> {
                if (pins.size() == 50) {
                    context.replyI18n("error.pin_max");
                } else {
                    togglePin(context, context.getChannel(), context.getArguments()[0]);
                    context.replyI18n("success.done");
                }
            });
        }
    }

    /**
     * Toggles the 'pinned' state of a given message in a given channel.
     * @param context The context of the issued command.
     * @param channel The channel the message is in.
     * @param messageID The ID of the message to pin or unpin.
     */
    private void togglePin(CommandContext context, MessageChannel channel, String messageID) {
        if(channel.retrieveMessageById(messageID).complete().isPinned()) {
            channel.unpinMessageById(messageID)
                    .queue(null, err -> context.replyI18n("error.pin_channel"));
        } else {
            channel.pinMessageById(messageID)
                    .queue(null, err -> context.replyI18n("error.pin_channel"));
        }
    }

    /**
     * Gets the list of subject channels containing ID strings.
     * @return The String list of subject channel IDs.
     */
    private List<Long> getSubjectChannels() {
        return IBai.INSTANCE.getConfig().getSubjects().getSubjects().stream()
                .map(Tuple::getPropertyB)
                .collect(Collectors.toList());
    }
}
