/**
 * Copyright 2017-2019 Arraying
 *
 * This file is part of LoyalBot.
 *
 * LoyalBot is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LoyalBot is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LoyalBot. If not, see http://www.gnu.org/licenses/.
 */

package dev.vardy.utils;

import dev.vardy.LoyalBot;
import dev.vardy.data.db.DContainer;
import dev.vardy.data.db.entries.GuildData;
import de.arraying.gravity.Gravity;
import net.dv8tion.jda.core.entities.Guild;

public final class UDatabase {

    /**
     * Util method to get the prefix for a guild.
     * @param guild The guild.
     * @return A never null prefix (fallbacks to config if required).
     */
    public static String getPrefix(Guild guild) {
        Gravity gravity = DContainer.INSTANCE.getGravity();
        return gravity.load(new GuildData(guild.getId()))
            .get(GuildData.PREFIX)
            .defaulting(LoyalBot.INSTANCE.getConfig().getStaticPrefix())
            .asString();
    }

}
