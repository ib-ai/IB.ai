package com.ibdiscord.pagination;

import java.util.LinkedList;
import java.util.List;

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
            return page(total);
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
