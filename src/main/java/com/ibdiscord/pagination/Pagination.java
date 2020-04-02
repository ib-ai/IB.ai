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

package com.ibdiscord.pagination;

import java.util.LinkedList;
import java.util.List;

public final class Pagination<T> {

    private final List<T> entries;
    private final int perPage;
    private final int total;

    /**
     * Creates a new pagination.
     * @param entries The total entries.
     * @param perPage The number of entries per page.
     */
    public Pagination(List<T> entries, int perPage) {
        this.entries = entries;
        this.perPage = perPage;
        if(entries.size() % perPage == 0) {
            total = entries.size() / perPage;
        } else {
            total = (entries.size() / perPage) + 1;
        }
    }

    /**
     * Gets a specific page.
     * @param number The page number, will reset to min/max if out of bounds.
     * @return A list of elements.
     */
    public List<Page<T>> page(int number) {
        if(number < 1) {
            return page(1);
        }
        if(number > total) {
            number = this.total;
        }
        int start = entries.size() < perPage ? 0 : (number * perPage) - perPage;
        int end = start + perPage <= entries.size() ? start + perPage : entries.size();
        List<Page<T>> entries = new LinkedList<>();
        for(int i = start; i < end; i++) {
            entries.add(new Page<>(i + 1, this.entries.get(i)));
        }
        return entries;
    }

    /**
     * Gets the total number of pages.
     * @return The total number of pages.
     */
    public int total() {
        return total;
    }

}
