/*******************************************************************************
 * Copyright 2018 pants
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.ibdiscord.data.db;

import com.ibdiscord.data.db.coordinator.DataType;
import com.ibdiscord.data.db.coordinator.Prefixion;

import io.lettuce.core.api.sync.RedisCommands;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */


/**
 * @author pants
 * @since 2018.08.21
 */

public class DController {

    private final RedisCommands sync;

    public DController() {
        sync = DContainer.getSync();
    }

    public String get(DataType type, String key) {
        Object value = sync.get(Prefixion.getPrefixedKey(type, key));
        return value == null ? null : value.toString();
    }

    public void set(DataType type, String key, Object value) {
        sync.set(Prefixion.getPrefixedKey(type, key), value.toString());
    }
}
