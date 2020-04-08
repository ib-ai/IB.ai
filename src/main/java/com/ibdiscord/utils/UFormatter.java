/* Copyright 2018-2020 Jarred Vardy <vardy@riseup.net>, Arraying
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
import com.ibdiscord.data.LocalConfig;
import net.dv8tion.jda.api.entities.User;
import org.slf4j.Logger;

public final class UFormatter {

    /**
     * Prints a splash screen.
     */
    public static void makeASplash() {
        StringBuilder banner = new StringBuilder();
        for(int i = 0; i < 60; i++) {
            banner.append("=");
        }
        Logger logger = IBai.INSTANCE.getLogger();
        LocalConfig config = IBai.INSTANCE.getConfig();
        logger.info(banner.toString());
        logger.info("");
        logger.info("IBai version {}", config.getBotVersion());
        logger.info("Developed by:");
        for(String developer : config.getBotAuthors()) {
            logger.info("- {}", developer);
        }
        logger.info("");
        logger.info(banner.toString());
    }

    /**
     * Formats a user to a user friendly name#discrim form.
     * @param user The user.
     * @return The user's name as a tag (formatted).
     */
    public static String formatMember(User user) {
        return user.getAsTag();
    }

}
