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

package dev.vardy.data.db.entries.punish;

import de.arraying.gravity.data.types.TypeMap;

import lombok.AllArgsConstructor;

public final @AllArgsConstructor class PunishmentData extends TypeMap {

    /**
     * The type.
     */
    public static final String TYPE = "type";

    /**
     * The user display string.
     */
    public static final String USER_DISPLAY = "user_display";

    /**
     * The user ID.
     */
    public static final String USER_ID = "user_id";

    /**
     * The staff display string.
     */
    public static final String STAFF_DISPLAY = "staff_display";

    /**
     * The staff ID.
     */
    public static final String STAFF_ID = "staff_id";

    /**
     * The reason.
     */
    public static final String REASON = "reason";

    /**
     * Present when user sensitive information is redacted.
     */
    public static final String REDACTED = "redacted";

    /**
     * The message ID.
     */
    public static final String MESSAGE = "message_id";

    private final String guild;
    private final Object caseId;

    /**
     * Gets the unique identifier.
     * @return The unique identifier.
     */
    @Override
    protected String getUniqueIdentifier() {
        return "case_" + guild + "_" + caseId;
    }

}
