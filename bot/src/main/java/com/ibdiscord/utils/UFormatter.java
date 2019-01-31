/*******************************************************************************
 * Copyright 2018 Jarred Vardy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.ibdiscord.utils;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

import com.ibdiscord.IBai;
import net.dv8tion.jda.core.entities.Member;

/** @author vardy, Arraying
 * @since 2018.08.18
 */

public final class UFormatter {

    private static final String DEFAULT_REASON = "Use `%sReason [Case Number]` to append a reason.";

    private String splashPath;
    private String author;
    private String[] contributors;

    public static void makeASplash() {
        //TODO: Splashscreen
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
