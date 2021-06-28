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

package com.ibdiscord.ibai.repositories;

import com.ibdiscord.ibai.entities.UserRole;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * The repository for all sticky roles.
 */
public interface UserRoleRepository extends CrudRepository<UserRole, Long> {

    /**
     * Gets all instances by user.
     * @param user The user ID.
     * @return A list of matches.
     */
    List<UserRole> findByUser(long user);

    /**
     * Deletes all instances by user.
     * @param user The user ID.
     */
    void deleteByUser(long user);

}
