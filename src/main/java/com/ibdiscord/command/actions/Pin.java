/* Copyright 2018-2020 Jarred Vardy
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
import com.ibdiscord.utils.UJSON;
import de.arraying.kotys.JSON;
import de.arraying.kotys.JSONArray;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.List;

public final class Pin implements CommandAction {

    private final List<String> SUBJECT_CHANNELS = this.getSubjectChannels();

    /**
     * Pins a message in its channel.
     * @param context The command's context.
     */
    @Override
    public void accept(CommandContext context) {
        context.assertArguments(1, "error.too_few_args"); // _At least_ one arg.
        if(context.getArguments().length == 2) { // Channel is specified by user
            TextChannel channel = context.assertChannel(context.getArguments()[0], "error.reaction_channel");
            String msgID = context.getArguments()[1];
            context.assertID(msgID, "error.reaction_message");
            if(!SUBJECT_CHANNELS.contains(channel.getId())) {
                context.replyI18n("error.subject_channel");
                return;
            }
            togglePin(context, channel, context.getArguments()[1]);
            context.replyI18n("success.done");
        } else { // Channel is unspecified. Uses channel the user issued the command from.
            context.assertID(context.getArguments()[0], "error.pin_channel");
            if(!SUBJECT_CHANNELS.contains(context.getChannel().getId())) {
                context.replyI18n("error.subject_channel");
                return;
            }
            togglePin(context, context.getChannel(), context.getArguments()[0]);
            context.replyI18n("success.done");
        }
    }

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
    private List<String> getSubjectChannels() {
        JSON penis = UJSON.retrieveJSONFromFile("/IB.ai/lang/subject_channels.json");
        JSONArray channels = penis.array("channels");
        List<String> listOfChannelIDs = new ArrayList<>();
        for(int i = 0; i < channels.length(); i++) {
            JSON jsonObj = channels.json(i);
            listOfChannelIDs.add(jsonObj.string("id"));
        }
        return listOfChannelIDs;
    }
}
