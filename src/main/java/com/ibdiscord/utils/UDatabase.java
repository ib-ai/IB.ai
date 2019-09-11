/* Copyright 2017-2019 Arraying
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

package com.ibdiscord.utils;

import com.ibdiscord.IBai;
import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.GuildData;
import de.arraying.gravity.Gravity;
import net.dv8tion.jda.api.entities.Guild;

public final class UDatabase {

    /**
     * Util method to get the prefix for a guild.
     * @param guild The guild.
     * @return A never null prefix (fallbacks to config if required).
     */
    public static String getPrefix(Guild guild) {
        Gravity gravity = DataContainer.INSTANCE.getGravity();
        return gravity.load(new GuildData(guild.getId()))
            .get(GuildData.PREFIX)
            .defaulting(IBai.INSTANCE.getConfig().getStaticPrefix())
            .asString();
    }

}
