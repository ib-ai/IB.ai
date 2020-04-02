/* Copyright 2018-2020 Arraying
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

package com.ibdiscord.punish;

import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.punish.PunishmentData;
import com.ibdiscord.data.db.entries.punish.PunishmentsData;
import com.ibdiscord.utils.UDatabase;
import de.arraying.gravity.Gravity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Guild;

import static com.ibdiscord.data.db.entries.punish.PunishmentData.*;

@AllArgsConstructor
@Getter
public final class Punishment {

    private static final String DEFAULT_REASON = "Use `%sreason %d <reason>` to specify a reason.";

    @Setter private PunishmentType type;
    private final String userDisplay;
    private final String userId;
    private final String staffDisplay;
    private final String staffId;
    @Setter private String reason;
    private boolean redacted;

    /**
     * Gets a punishment wrapper from the database.
     * @param guild The guild.
     * @param caseNumber The case number.
     * @return A valid punishment wrapper.
     */
    public static Punishment of(Guild guild, Object caseNumber) {
        PunishmentData data = DataContainer.INSTANCE.getGravity().load(new PunishmentData(guild.getId(), caseNumber));
        return new Punishment(
                PunishmentType.valueOf(data.get(TYPE).asString()),
                data.get(USER_DISPLAY).asString(),
                data.get(USER_ID).asString(),
                data.get(STAFF_DISPLAY).asString(),
                data.get(STAFF_ID).asString(),
                data.get(REASON).asString(),
                data.get(REDACTED).defaulting(false).asBoolean()
        );
    }

    /**
     * Dumps a punishment to the database, adding its case number to a registry of all cases.
     * @param guild The guild.
     * @param caseNumber The case number.
     */
    void dump(Guild guild, long caseNumber) {
        Gravity gravity = DataContainer.INSTANCE.getGravity();
        PunishmentData punishment = gravity.load(new PunishmentData(guild.getId(), caseNumber));
        punishment.set(TYPE, type.toString());
        punishment.set(USER_DISPLAY, userDisplay);
        punishment.set(USER_ID, userId);
        punishment.set(STAFF_DISPLAY, staffDisplay);
        punishment.set(STAFF_ID, staffId);
        punishment.set(REASON, reason == null ? getDefaultReason(guild, caseNumber) : reason);
        punishment.set(REDACTED, redacted);
        gravity.save(punishment);

        PunishmentsData list = gravity.load(new PunishmentsData(guild.getId()));
        list.add(caseNumber);
        gravity.save(list);
    }

    /**
     * Sets the redacting value.
     * @param value The value.
     * @return The punishment.
     */
    public Punishment redacting(boolean value) {
        this.redacted = value;
        return this;
    }

    /**
     * Gets the punishment log message.
     * @param guild The guild.
     * @param caseNumber Case number of the retrievable punishment.
     * @return The message.
     */
    public String getLogPunishment(Guild guild, long caseNumber) {
        String modLog = String.format("**Case: #%d | %s**\n**Offender: **%s (User: %s, ID: %s)\n**Moderator: **%s "
                        + "(ID: %s)\n**Reason: **%s",
                caseNumber,
                type.getDisplayInitial(),
                getSensitiveField(getMention()),
                getSensitiveField(userDisplay),
                getSensitiveField(userId),
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
    @SuppressWarnings("WeakerAccess")
    public String getLogRevocation() {
        return String.format("**%s**\n**Pardoned: **%s (User: %s, ID: %s)\n**Moderator: **%s (ID: %s)",
                type.getDisplayRevocation(),
                getMention(),
                userDisplay,
                userId,
                staffDisplay,
                staffId);
    }

    /**
     * Gets a sensitive field, that is formatted appropriately.
     * @param field The field.
     * @return A censored version, if applicable.
     */
    private String getSensitiveField(String field) {
        return redacted ? "[REDACTED]" : field;
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

    /**
     * Gets the user as a mention.
     * @return The user mention.
     */
    private String getMention() {
        return String.format("<@%s>", userId);
    }

}
