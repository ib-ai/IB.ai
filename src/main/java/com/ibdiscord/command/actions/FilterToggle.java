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
import de.arraying.gravity.Gravity;

public final class FilterToggle implements CommandAction {

    /**
     * Toggles the filter.
     * @param context The command context.
     */
    @Override
    public void accept(CommandContext context) {
        Gravity gravity = DataContainer.INSTANCE.getGravity();
        GuildData guildData = gravity.load(new GuildData(context.getGuild().getId()));
        boolean current = guildData.get(GuildData.FILTERING).defaulting(false).asBoolean();
        guildData.set(GuildData.FILTERING, !current);
        gravity.save(guildData);
        context.replyI18n(current ? "success.filter_disable" : "success.filter_enable");
    }

}
