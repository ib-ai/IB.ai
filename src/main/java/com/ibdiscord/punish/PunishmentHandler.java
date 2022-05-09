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
import com.ibdiscord.data.db.entries.GuildData;
import com.ibdiscord.data.db.entries.punish.PunishmentData;
import com.ibdiscord.data.db.entries.punish.PunishmentsData;
import de.arraying.gravity.Gravity;
import java.utils.Collections;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

@AllArgsConstructor
public final class PunishmentHandler {

    private final Guild guild;
    private final Punishment punishment;

    /**
     * Executes the punishment and sends the message.
     */
    public void onPunish() {
        Gravity gravity = DataContainer.INSTANCE.getGravity();
        long caseNumber = Collections.max(gravity.load(new PunishmentsData(guild.getId())).values()) + 1;
        punishment.dump(guild, caseNumber);
        TextChannel channel = getLogChannel(GuildData.MODLOGS);
        if(channel == null) {
            return;
        }
        channel.sendMessage(punishment.getLogPunishment(guild, caseNumber)).queue(success -> {
            PunishmentData punishmentData = gravity.load(new PunishmentData(guild.getId(), caseNumber));
            punishmentData.set(PunishmentData.MESSAGE, success.getIdLong());
            gravity.save(punishmentData);
        });
        TextChannel staff_channel = getLogChannel(GuildData.MODLOGS_STAFF);
        if(staff_channel == null) {
            return;
        }
        staff_channel.sendMessage(punishment
                .redacting(false)
                .getLogPunishment(guild, caseNumber))
                .queue();
    }

    /**
     * Executes a revocation.
     * When doing this, the data inside the punishment can be null.
     * The only required data is the type, user display/id, staff display/id.
     */
    public void onRevocation() {
        TextChannel channel = getLogChannel(GuildData.MODLOGS);
        if(channel != null) {
            channel.sendMessage(punishment.getLogRevocation()).queue();
        }
    }

    /**
     * Gets the log channel.
     * @return The possibly null log channel.
     */
    public TextChannel getLogChannel(String modlogs) {
        GuildData guildData = DataContainer.INSTANCE.getGravity().load(new GuildData(guild.getId()));
        return guild.getTextChannelById(guildData.get(modlogs)
                .defaulting(0L)
                .asLong());
    }

}
