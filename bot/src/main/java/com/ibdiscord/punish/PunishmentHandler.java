package com.ibdiscord.punish;

import com.ibdiscord.data.db.DContainer;
import com.ibdiscord.data.db.entries.GuildData;
import com.ibdiscord.data.db.entries.punish.PunishmentData;
import com.ibdiscord.data.db.entries.punish.PunishmentsData;
import com.ibdiscord.utils.UDatabase;
import de.arraying.gravity.Gravity;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;

import static com.ibdiscord.command.commands.ModLogCommand.DISABLED_MOD_LOG;
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
public final @AllArgsConstructor class PunishmentHandler {

    /**
     * The default reason.
     */
    private static final String DEFAULT_REASON = "Use `%sReason [Case Number]` to append a reason.";

    private final Guild guild;
    private final PunishmentWrapper wrapper;

    /**
     * Creates a new punishment.
     * This should be executed after the punishment was successful, and for logging punishments.
     */
    public void create() {
        String guildId = guild.getId();
        Gravity gravity = DContainer.INSTANCE.getGravity();
        PunishmentsData punishmentList = gravity.load(new PunishmentsData(guildId));
        long caseNumber = punishmentList.size() + 1;
        punishmentList.add(caseNumber);
        PunishmentData punishmentData = gravity.load(new PunishmentData(guildId, caseNumber));
        punishmentData.set(CASE, caseNumber);
        punishmentData.set(TYPE, wrapper.getType());
        punishmentData.set(USER_DISPLAY, wrapper.getUserDisplay());
        punishmentData.set(USER_ID, wrapper.getUserId());
        punishmentData.set(STAFF_DISPLAY, wrapper.getStaffDisplay());
        punishmentData.set(STAFF_ID, wrapper.getStaffId());
        if(wrapper.getReason() == null) {
            wrapper.setReason(String.format(DEFAULT_REASON, UDatabase.getPrefix(guild)));
        }
        punishmentData.set(REASON, wrapper.getReason());
        TextChannel channel = getModLogChannel();
        if(channel != null) {
            channel.sendMessage(getMogLogMessage(caseNumber)).queue(
                    wellPlayedSir -> punishmentData.set(MESSAGE, wellPlayedSir.getId())
            );
        }
        gravity.save(punishmentData);
        gravity.save(punishmentList);
    }

    /**
     * Gets the mod log channel.
     * @return The channel, or null if it does not exist.
     */
    public TextChannel getModLogChannel() {
        GuildData guildData = DContainer.INSTANCE.getGravity().load(new GuildData(guild.getId()));
        return guild.getTextChannelById(guildData.get(GuildData.MODLOGS)
                .defaulting(DISABLED_MOD_LOG)
                .asLong()
        );
    }

    /**
     * Makes the mod log string.
     * @param caseNumber The case number.
     * @return The mod log string.
     */
    public String getMogLogMessage(long caseNumber) {
        String modLog = String.format("**Case: #%d | %s**\n**Offender: **%s (ID: %s)\n**Moderator: **%s (ID: %s)\n**Reason: **%s",
                caseNumber,
                wrapper.getType().getDisplayInitial(),
                wrapper.getUserDisplay(),
                wrapper.getUserId(),
                wrapper.getStaffDisplay(),
                wrapper.getStaffId(),
                wrapper.getReason());
        if(modLog.length() > 2000) {
            modLog = modLog.substring(0, 2000);
        }
        return modLog;
    }

    /**
     * Makes the mod log revocation string.
     * @param caseNumber The case number.
     * @return The mod log revocation strong.
     */
    public String getModLogMessageRevocation(long caseNumber) {
        return String.format("**Case: #%d | %s**\n**Pardoned: **%s (ID: %s)\n**Moderator: **%s (ID: %s)",
                caseNumber,
                wrapper.getType().getDisplayRevocation(),
                wrapper.getUserDisplay(),
                wrapper.getUserId(),
                wrapper.getStaffDisplay(),
                wrapper.getStaffId());
    }

}
