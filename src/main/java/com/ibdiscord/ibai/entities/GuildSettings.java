/* Copyright 2017-2021 Arraying
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

package com.ibdiscord.ibai.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Contains all the settings for the guild.
 */
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "settings")
public final class GuildSettings {

    @Value("${discord.server}")
    @Id private long id;

    @Column(name = "prefix")
    private String prefix;

    @Column(name = "modlog_id")
    private long modLog;

    @Column(name = "updates_id")
    private long updates;

    @Column(name = "logs_id")
    private long logs;

    @Column(name = "mute_role_id")
    private long muteRole;

    @Column(name = "helper_id")
    private long helper;

    @Column(name = "staff")
    private long staff;

    /**
     * Creates the entity.
     * @param prefix The prefix.
     * @param modLog The mod log ID.
     * @param updates The updates ID.
     * @param logs The logs ID.
     * @param muteRole The mute role ID.
     * @param helper The helper role ID.
     * @param staff The staff role ID.
     */
    public GuildSettings(String prefix, long modLog, long updates, long logs, long muteRole, long helper, long staff) {
        this.prefix = prefix;
        this.modLog = modLog;
        this.updates = updates;
        this.logs = logs;
        this.muteRole = muteRole;
        this.helper = helper;
        this.staff = staff;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GuildSettings that = (GuildSettings) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
