package com.ibdiscord.punish;

import com.ibdiscord.data.db.DContainer;
import com.ibdiscord.data.db.entries.punish.PunishmentData;
import com.ibdiscord.data.db.entries.punish.PunishmentsData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import static com.ibdiscord.data.db.entries.punish.PunishmentData.*;

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
public final @Getter @AllArgsConstructor class PunishmentWrapper {

    private final PunishmentType type;
    private final String userDisplay;
    private final String userId;
    private final String staffDisplay;
    private final String staffId;
    @Setter String reason;

    /**
     * Gets a punishment wrapper from the database.
     * @param guild The guild.
     * @param caseNumber The case number.
     * @return A valid punishment wrapper.
     */
    public static PunishmentWrapper of(String guild, long caseNumber) {
        PunishmentData data = DContainer.INSTANCE.getGravity().load(new PunishmentData(guild, caseNumber));
        return new PunishmentWrapper(
                PunishmentType.valueOf(data.get(TYPE).asString()),
                data.get(USER_DISPLAY).asString(),
                data.get(USER_ID).asString(),
                data.get(STAFF_DISPLAY).asString(),
                data.get(STAFF_ID).asString(),
                data.get(REASON).defaulting("").asString()
        );
    }

    /**
     * Dumps a punishment to the database, adding its case number to a registry of all cases.
     * @param guild The guild.
     * @param caseNumber The case number.
     */
    public void dump(String guild, long caseNumber) {
        PunishmentsData list = DContainer.INSTANCE.getGravity().load(new PunishmentsData(guild));
        PunishmentData punishment = DContainer.INSTANCE.getGravity().load(new PunishmentData(guild, caseNumber));
        punishment.set(TYPE, type.toString());
        punishment.set(USER_DISPLAY, userDisplay);
        punishment.set(USER_ID, userId);
        punishment.set(STAFF_DISPLAY, staffDisplay);
        punishment.set(STAFF_ID, staffId);
        punishment.set(REASON, reason);
        list.add(caseNumber);
    }

}
