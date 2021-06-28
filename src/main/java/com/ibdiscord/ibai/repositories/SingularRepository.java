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

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Iterator;
import java.util.Optional;

/**
 * A generic repository that only contains one row.
 * This is annotated so that Spring doesn't try to initialize this as an actual repository.
 * @param <T> The entity.
 * @param <ID> The primary key.
 */
@NoRepositoryBean
public interface SingularRepository<T, ID> extends CrudRepository<T, ID> {

    /**
     * Gets the value.
     * @return An optional containing the value, if present.
     */
    default Optional<T> get() {
        Iterator<T> iterator = findAll().iterator();
        return !iterator.hasNext() ? Optional.empty() : Optional.of(iterator.next());
    }
}
