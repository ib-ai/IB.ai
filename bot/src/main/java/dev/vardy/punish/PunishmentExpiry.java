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
import dev.vardy.data.db.entries.punish.ExpiryData;
import de.arraying.gravity.Gravity;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

import java.util.Map;
import java.util.concurrent.*;

public enum PunishmentExpiry {

    INSTANCE;

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private final Map<String, ScheduledFuture<?>> schedules = new ConcurrentHashMap<>();

    public ScheduledFuture<?> getFor(String caseNumber) {
        return schedules.get(caseNumber);
    }

    /**
     * Schedules a punishment expiration.
     * @param guild The guild.
     * @param caseNumber The case number.
     * @param delay The delay until the expiration.
     * @param punishment The punishment.
     */
    public void schedule(Guild guild, String caseNumber, long delay, Punishment punishment) {
        schedules.put(caseNumber, executorService.schedule(() -> expire(guild, caseNumber, punishment), delay, TimeUnit.MILLISECONDS));
    }

    /**
     * Expires a punishment.
     * @param guild The guild.
     * @param caseNumber The case number.
     * @param punishment The punishment.
     */
    public void expire(Guild guild, String caseNumber, Punishment punishment) {
        Gravity gravity = DContainer.INSTANCE.getGravity();
        ExpiryData expiryData = gravity.load(new ExpiryData(guild.getId()));
        expiryData.unset(caseNumber);
        gravity.save(expiryData);
        switch(punishment.getType()) {
            case MUTE:
                Member member = guild.getMemberById(punishment.getUserId());
                if(member == null) {
                    return;
                }
                GuildData guildData = DContainer.INSTANCE.getGravity().load(new GuildData(guild.getId()));
                Role role = guild.getRoleById(guildData.get(GuildData.MUTE).defaulting(0L).asLong());
                if(role == null) {
                    return;
                }
                guild.getController().removeSingleRoleFromMember(member, role).queue();
            case BAN:
                guild.getController().unban(punishment.getUserId()).queue();
        }
    }

}
