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

import javax.persistence.*;

/**
 * Represents user roles that will be sticky when the user re-joins the server.
 * Each user may have multiple roles associated with them.
 */
@NoArgsConstructor
@Getter
@Entity
@Table(name = "user_roles", indexes = @Index(columnList = "user_id"))
public final class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "user_id")
    private long user;

    @Column(name = "role_id")
    private long role;

    /**
     * Creates an entity.
     * @param user The user ID.
     * @param role The role ID.
     */
    public UserRole(long user, long role) {
        this.user = user;
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserRole userRole = (UserRole) o;

        if (user != userRole.user) return false;
        return role == userRole.role;
    }

    @Override
    public int hashCode() {
        int result = (int) (user ^ (user >>> 32));
        result = 31 * result + (int) (role ^ (role >>> 32));
        return result;
    }
}
