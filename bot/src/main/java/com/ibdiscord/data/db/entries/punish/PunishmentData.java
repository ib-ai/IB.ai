package com.ibdiscord.data.db.entries.punish;

import de.arraying.gravity.data.types.TypeMap;
import lombok.AllArgsConstructor;

/**
 * Copyright 2019 Arraying
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
