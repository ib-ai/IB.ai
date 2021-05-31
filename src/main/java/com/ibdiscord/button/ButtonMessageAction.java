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

package com.ibdiscord.button;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.dv8tion.jda.internal.requests.Route;
import net.dv8tion.jda.internal.requests.restaction.MessageActionImpl;

import java.util.List;

public final class ButtonMessageAction extends MessageActionImpl {

    private final List<ButtonRole> buttons;

    /**
     * Creates the new action.
     * @param api The API.
     * @param route The route.
     * @param channel The channel.
     * @param message The message to send.
     * @param buttons The buttons.
     */
    private ButtonMessageAction(JDA api, Route.CompiledRoute route, MessageChannel channel, String message, List<ButtonRole> buttons) {
        super(api, route, channel, new StringBuilder().append(message));
        this.buttons = buttons;
    }

    /**
     * Creates a message which has button reactions.
     * This will use one action row.
     * @param channel The channel.
     * @param message The message to send.
     * @param roles A list of button reaction roles.
     * @return An action, which can be queued.
     */
    public static ButtonMessageAction create(TextChannel channel, String message, List<ButtonRole> roles) {
        Route.CompiledRoute route = Route.Messages.SEND_MESSAGE.compile(channel.getId());
        return new ButtonMessageAction(channel.getJDA(), route, channel, message, roles);
    }

    /**
     * Gets the message as JSON.
     * Here, we inject the custom button stuff.
     * @return A data object of messages.
     */
    @Override
    protected DataObject getJSON() {
        final DataObject dataObject = super.getJSON();
        final DataArray componentsOuter = DataArray.empty();
        for (int i = 0; i < 5; i++) {
            final DataObject actionRow = DataObject.empty();
            final DataArray componentsInner = DataArray.empty();
            for (ButtonRole button : buttons) {
                if (button.getRow() != i) {
                    continue;
                }
                DataObject clickable = DataObject.empty();
                clickable.put("type", 2);
                clickable.put("label", button.getName());
                clickable.put("style", button.getColour().ordinal() + 1);
                clickable.put("custom_id", button.getRoles());
                if (button.getEmoji() != null) {
                    clickable.put("emoji", button.getEmoji().get());
                }
                componentsInner.add(clickable);
            }
            if (componentsInner.length() > 0) {
                actionRow.put("type", 1);
                actionRow.put("components", componentsInner);
                componentsOuter.add(actionRow);
            }
        }
        dataObject.put("components", componentsOuter);
        return dataObject;
    }
}
