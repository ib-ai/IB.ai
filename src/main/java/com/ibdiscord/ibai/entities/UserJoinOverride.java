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

/**
 * Represents a user join override.
 * This just consists of a custom number to use for the override.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "user_overrides")
public final class UserJoinOverride {

    @Id
    @Column(name = "user_id")
    private long user;

    @Column(name = "override")
    private int override;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserJoinOverride that = (UserJoinOverride) o;

        return user == that.user;
    }

    @Override
    public int hashCode() {
        return (int) (user ^ (user >>> 32));
    }
}
