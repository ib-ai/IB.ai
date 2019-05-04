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

package dev.vardy.punish;

import dev.vardy.data.db.DContainer;
import dev.vardy.data.db.entries.GuildData;
import dev.vardy.data.db.entries.punish.PunishmentData;
import dev.vardy.data.db.entries.punish.PunishmentsData;
import de.arraying.gravity.Gravity;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;

public final @AllArgsConstructor class PunishmentHandler {

    private final Guild guild;
    private final Punishment punishment;

    /**
     * Executes the punishment and sends the message.
     */
    public void onPunish() {
        Gravity gravity = DContainer.INSTANCE.getGravity();
        long caseNumber = gravity.load(new PunishmentsData(guild.getId())).size() + 1;
        punishment.dump(guild, caseNumber);
        TextChannel channel = getLogChannel();
        if(channel == null) {
            return;
        }
        channel.sendMessage(punishment.getLogPunishment(guild, caseNumber)).queue(success -> {
            PunishmentData punishmentData = gravity.load(new PunishmentData(guild.getId(), caseNumber));
            punishmentData.set(PunishmentData.MESSAGE, success.getIdLong());
            gravity.save(punishmentData);
        });
    }

    /**
     * Executes a revocation.
     * When doing this, the data inside the punishment can be null.
     * The only required data is the type, user display/id, staff display/id.
     */
    public void onRevocation() {
        TextChannel channel = getLogChannel();
        if(channel != null) {
            channel.sendMessage(punishment.getLogRevocation()).queue();
        }
    }

    /**
     * Gets the log channel.
     * @return The possibly null log channel.
     */
    public TextChannel getLogChannel() {
        GuildData guildData = DContainer.INSTANCE.getGravity().load(new GuildData(guild.getId()));
        return guild.getTextChannelById(guildData.get(GuildData.MODLOGS)
                .defaulting(0L)
                .asLong());
    }

}
