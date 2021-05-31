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

import com.ibdiscord.button.ButtonColour;
import com.ibdiscord.button.ButtonEmoji;
import com.ibdiscord.button.ButtonMessageAction;
import com.ibdiscord.command.CommandAction;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.utils.UInput;
import com.ibdiscord.utils.UString;
import de.arraying.kotys.JSON;
import de.arraying.kotys.JSONArray;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        if (data.isEmpty()) {
            context.replyI18n("error.buttonrole_integrity");
            return;
        }
        List<com.ibdiscord.button.ButtonRole> buttons = new ArrayList<>();
        //int emojiIndex = 0;
        Set<String> known = new HashSet<>();
        for (int i = 0; i < data.length(); i++) {
            JSON entity = data.json(i);
            ButtonColour colour;
            try {
                colour = ButtonColour.valueOf(entity.string("colour").toUpperCase());
            } catch (IllegalArgumentException | NullPointerException exception) {
                context.replyI18n("error.buttonrole_integrity");
                return;
            }
            ButtonEmoji emoji = null;
            if (entity.has("emoji")) {
                emoji = ButtonEmoji.parse(entity.string("emoji"));
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
            String name = entity.string("label");
            if (name == null || name.isEmpty() || name.length() > 80
                    || roleSplit.length == 0 || roleSplit.length > 4) {
                context.replyI18n("error.buttonrole_integrity");
                return;
            }
            buttons.add(new com.ibdiscord.button.ButtonRole(emoji, colour, name, roles));
            known.add(roles);
        }
        ButtonMessageAction.create(context.assertChannel(context.getArguments()[0], "error.invalid_data"), buttons)
            .queue(yes -> context.replyI18n("success.done"), no -> {
                context.replyI18n("error.generic");
                no.printStackTrace();
            });
    }
}
