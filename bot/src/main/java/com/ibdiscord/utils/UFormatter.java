package com.ibdiscord.utils;

import com.ibdiscord.IBai;
import com.ibdiscord.data.LocalConfig;
import net.dv8tion.jda.core.entities.Member;
import org.slf4j.Logger;

/**
 * Copyright 2018 Jarred Vardy, Arraying
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public final class UFormatter {

    private static final String DEFAULT_REASON = "Use `%sReason [Case Number]` to append a reason.";

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
     * Makes a mod log message with the given parameters.
     * @param handle The case number.
     * @param display The display name for the punishment.
     * @param userDisplay The display name of the user.
     * @param userId The ID of the user.
     * @param staffDisplay The display name of the staff member.
     * @param staffId The ID of the staff member.
     * @param reason The reason for punishing. If null, no reason will be specified.
     * @return A mod long message.
     */
    public static String makeAModLog(long handle, String display, String userDisplay, String userId, String staffDisplay, String staffId, String reason) {
        String modLog = String.format("**Case: #%d | %s**\n**Offender: **%s (ID: %s)\n**Moderator: **%s (ID: %s)\n**Reason: **%s",
                handle,
                display,
                userDisplay,
                userId,
                staffDisplay,
                staffId,
                reason == null ? String.format(DEFAULT_REASON, IBai.INSTANCE.getConfig().getStaticPrefix()) : reason);
        if(modLog.length() > 2000) {
            modLog = modLog.substring(0, 2000);
        }
        return modLog;
    }

    /**
     * Formats a member to a user friendly name#discrim form.
     * @param member The member.
     * @return The user, formatted.
     */
    public static String formatMember(Member member) {
        return member.getUser().getName() + "#" + member.getUser().getDiscriminator();
    }

}
