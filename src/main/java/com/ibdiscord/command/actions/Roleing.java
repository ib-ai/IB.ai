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
import com.ibdiscord.utils.UString;
import de.arraying.gravity.Gravity;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class Roleing implements CommandAction {

    private final String field;
    private final String key;

    /**
     * Sets a specific role to be something.
     * @param context The command context.
     */
    @Override
    public void accept(CommandContext context) {
        Gravity gravity = DataContainer.INSTANCE.getGravity();
        GuildData guildData = gravity.load(new GuildData(context.getGuild().getId()));
        if(context.getArguments().length == 0) {
            String permission = guildData.get(field)
                    .defaulting("not set")
                    .asString();
            context.replyI18n("info." + key, permission);
            return;
        }
        String newValue = UString.concat(context.getArguments(), " ", 0).trim();
        guildData.set(field, newValue);
        gravity.save(guildData);
        context.replyI18n("success." + key);
    }

}
