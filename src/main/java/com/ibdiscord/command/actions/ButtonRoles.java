/* Copyright 2017-2021 Arraying
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

import com.ibdiscord.button.ButtonRole;
import com.ibdiscord.command.CommandAction;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.utils.UInput;
import com.ibdiscord.utils.UString;
import de.arraying.kotys.JSON;
import de.arraying.kotys.JSONArray;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;

import java.util.*;
import java.util.stream.Collectors;

public final class ButtonRoles implements CommandAction {
    @Override
    public void accept(CommandContext context) {
        context.assertArguments(1, "error.missing_channel");
        if (context.getMessage().getMentionedChannels().size() < 1) {
            context.replyI18n("error.missing_channel");
            return;
        }
        context.assertArguments(2, "error.missing_data");
        String json = UInput.escapeLinebreakInQuotations(UString.concat(context.getArguments(), " ", 1));
        JSONArray data;
        try {
            data = new JSONArray(json);
        } catch (IllegalStateException e) {
            context.replyI18n("error.missing_data");
            return;
        }
        if (data.length() < 2 || data.length() > 26) {
            context.replyI18n("error.buttonrole_integrity");
            return;
        }
        String message = data.string(0);
        List<ButtonRole> buttons = new ArrayList<>();
        Set<String> known = new HashSet<>();
        for (int i = 1; i < data.length(); i++) {
            JSON entity = data.json(i);
            ButtonStyle colour;
            try {
                colour = ButtonStyle.valueOf(entity.string("colour").toUpperCase());
                if (colour == ButtonStyle.UNKNOWN || colour == ButtonStyle.LINK) {
                    throw new IllegalArgumentException();
                }
            } catch (IllegalArgumentException | NullPointerException exception) {
                context.replyI18n("error.buttonrole_integrity");
                return;
            }
            Emoji emoji = null;
            if (entity.has("emoji")) {
                String emojiString = entity.string("emoji");
                if (emojiString != null && !emojiString.trim().isEmpty()) {
                    String[] emojiData = emojiString.split(" ");
                    if (emojiData.length == 2) {
                        emoji = Emoji.fromEmote(emojiData[0], Long.parseLong(emojiData[1]), false);
                    } else {
                        emoji = Emoji.fromMarkdown(emojiString.trim());
                    }
                }
            }
            String roles = entity.string("roles");
            if (known.contains(roles)) {
                context.replyI18n("error.buttonrole_integrity");
                return;
            }
            String[] roleSplit = roles.split(",");
            for (String role : roleSplit) {
                if (!UInput.isValidID(role)) {
                    context.replyI18n("error.buttonrole_integrity");
                    return;
                }
            }
            int row = 0;
            if (entity.has("row")) {
                row = entity.integer("row");
            }
            String name = entity.string("label");
            if (name == null || name.isEmpty() || name.length() > 80
                    || roleSplit.length == 0 || roleSplit.length > 4
                    || row < 0 || row > 4) {
                context.replyI18n("error.buttonrole_integrity");
                return;
            }
            buttons.add(new ButtonRole(emoji, colour, name, roles, row));
            known.add(roles);
        }
        TextChannel textChannel = context.assertChannel(context.getArguments()[0], "error.invalid_data");
        List<ActionRow> actionRows = buttons.stream()
            .collect(Collectors.groupingBy(ButtonRole::getRow)) // Group the roles by row.
            .entrySet() // Get each row + entry.
            .stream()
            .sorted(Map.Entry.comparingByKey()) // Sort the rows in ascending order
            .map(Map.Entry::getValue) // Convert to value.
            .map(values -> { // Convert values to action row.
                List<Button> buttonList = values.stream() // Get all buttons.
                    .map(singleButton -> { // Convert to JDA buttons.
                        return Button.of(
                            ButtonStyle.valueOf(singleButton.getColour().toString()),
                            singleButton.getRoles(),
                            singleButton.getName(),
                            singleButton.getEmoji()
                        );
                    })
                    .collect(Collectors.toList());
                return ActionRow.of(buttonList); // Create action row.
            })
            .collect(Collectors.toList()); // Collect as list.
        Message toSend = new MessageBuilder(message)
            .setActionRows(actionRows)
            .build();
        textChannel.sendMessage(toSend).queue(yes -> context.replyI18n("success.done"), no -> {
            context.replyI18n("error.generic");
            no.printStackTrace();
        });
    }
}
