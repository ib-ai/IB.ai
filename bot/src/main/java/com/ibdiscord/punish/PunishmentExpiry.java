package com.ibdiscord.punish;

import com.ibdiscord.data.db.DContainer;
import com.ibdiscord.data.db.entries.GuildData;
import com.ibdiscord.data.db.entries.punish.ExpiryData;
import de.arraying.gravity.Gravity;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

import java.util.Map;
import java.util.concurrent.*;

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
