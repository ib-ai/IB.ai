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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents a user opt.
 * Each user may have multiple opts associated with them.
 */
@NoArgsConstructor
@Getter
@Entity
@IdClass(UserOpt.CompositePK.class)
@Table(name = "user_opts", indexes = @Index(columnList = "user_id"))
public final class UserOpt {

    /**
     * Composite primary key.
     */
    @NoArgsConstructor
    @AllArgsConstructor
    public static final class CompositePK implements Serializable {
        private long user;
        private long channel;
    }

    @Id
    @Column(name = "user_id")
    private long user;

    @Id
    @Column(name = "channel_id")
    private long channel;

    /**
     * Creates an entity.
     * @param user The user ID.
     * @param channel The channel ID.
     */
    public UserOpt(long user, long channel) {
        this.user = user;
        this.channel = channel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserOpt userOpt = (UserOpt) o;

        if (user != userOpt.user) return false;
        return channel == userOpt.channel;
    }

    @Override
    public int hashCode() {
        int result = (int) (user ^ (user >>> 32));
        result = 31 * result + (int) (channel ^ (channel >>> 32));
        return result;
    }
}
