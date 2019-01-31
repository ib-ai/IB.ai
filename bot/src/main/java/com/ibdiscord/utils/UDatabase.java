package com.ibdiscord.utils;

import com.ibdiscord.IBai;
import com.ibdiscord.data.db.DContainer;
import com.ibdiscord.data.db.entries.GuildData;
import de.arraying.gravity.Gravity;
import net.dv8tion.jda.core.entities.Guild;

/**
 * Copyright 2018 Arraying
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
public final class UDatabase {

    public static String getPrefix(Guild guild) {
        Gravity gravity = DContainer.INSTANCE.getGravity();
        return gravity.load(new GuildData(guild.getId()))
            .get(GuildData.PREFIX)
            .defaulting(IBai.INSTANCE.getConfig().getStaticPrefix())
            .asString();
    }

}
