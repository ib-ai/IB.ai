package com.ibdiscord.data.db.entries;

import de.arraying.gravity.data.types.TypeMap;

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
public final class GuildData extends TypeMap {

    /**
     * The prefix key.
     */
    public static final String PREFIX = "prefix";

    /**
     * The modlogs key for Redis.
     */
    public static final String MODLOGS = "modlogs";

    private final String guild;

    /**
     * Creates the guild data.
     * @param guild The guild ID.
     */
    public GuildData(String guild) {
        this.guild = guild;
    }

    /**
     * Gets the identifier.
     * @return The identifier.
     */
    @Override
    protected String getUniqueIdentifier() {
        return "guild_" + guild;
    }

}
