package com.ibdiscord.punish;

import com.ibdiscord.data.db.DContainer;
import com.ibdiscord.data.db.entries.punish.PunishmentData;
import com.ibdiscord.data.db.entries.punish.PunishmentsData;
import com.ibdiscord.utils.UDatabase;
import de.arraying.gravity.Gravity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.core.entities.Guild;

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
public final @Getter @AllArgsConstructor class Punishment {

    private static final String DEFAULT_REASON = "Use `%sreason %d <reason>` to specify a reason.";

    @Setter private PunishmentType type;
    private final String userDisplay;
    private final String userId;
    private final String staffDisplay;
    private final String staffId;
    @Setter private String reason;

    /**
     * Gets a punishment wrapper from the database.
     * @param guild The guild.
     * @param caseNumber The case number.
     * @return A valid punishment wrapper.
     */
    public static Punishment of(Guild guild, Object caseNumber) {
        PunishmentData data = DContainer.INSTANCE.getGravity().load(new PunishmentData(guild.getId(), caseNumber));
        return new Punishment(
                PunishmentType.valueOf(data.get(TYPE).asString()),
                data.get(USER_DISPLAY).asString(),
                data.get(USER_ID).asString(),
                data.get(STAFF_DISPLAY).asString(),
                data.get(STAFF_ID).asString(),
                data.get(REASON).asString()
        );
    }

    /**
     * Dumps a punishment to the database, adding its case number to a registry of all cases.
     * @param guild The guild.
     * @param caseNumber The case number.
     */
    void dump(Guild guild, long caseNumber) {
        Gravity gravity = DContainer.INSTANCE.getGravity();
        PunishmentsData list = gravity.load(new PunishmentsData(guild.getId()));
        PunishmentData punishment = gravity.load(new PunishmentData(guild.getId(), caseNumber));
        punishment.set(TYPE, type.toString());
        punishment.set(USER_DISPLAY, userDisplay);
        punishment.set(USER_ID, userId);
        punishment.set(STAFF_DISPLAY, staffDisplay);
        punishment.set(STAFF_ID, staffId);
        punishment.set(REASON, reason == null ? getDefaultReason(guild, caseNumber) : reason);
        list.add(caseNumber);
        gravity.save(punishment);
        gravity.save(list);
    }

    /**
     * Gets the punishment log message.
     * @param guild The guild.
     * @return The message.
     */
    public String getLogPunishment(Guild guild, long caseNumber) {
        String modLog = String.format("**Case: #%d | %s**\n**Offender: **%s (ID: %s)\n**Moderator: **%s (ID: %s)\n**Reason: **%s",
                caseNumber,
                type.getDisplayInitial(),
                userDisplay,
                userId,
                staffDisplay,
                staffId,
                reason == null ? getDefaultReason(guild, caseNumber) : reason);
        if(modLog.length() > 2000) {
            modLog = modLog.substring(0, 2000);
        }
        return modLog;
    }

    /**
     * Gets the revocation log message.
     * @return The message.
     */
    public String getLogRevocation() {
        return String.format("**%s**\n**Pardoned: **%s (ID: %s)\n**Moderator: **%s (ID: %s)",
                type.getDisplayRevocation(),
                userDisplay,
                userId,
                staffDisplay,
                staffId);
    }

    /**
     * Gets the default reason for a guild.
     * @param guild The guild.
     * @param caseNumber The case number.
     * @return The default reason.
     */
    private String getDefaultReason(Guild guild, long caseNumber) {
        return String.format(DEFAULT_REASON, UDatabase.getPrefix(guild), caseNumber);
    }

}
