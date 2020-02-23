/* Copyright 2017-2020 Arraying
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

package com.ibdiscord.command.registrar;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.actions.FilterCreate;
import com.ibdiscord.command.actions.FilterDelete;
import com.ibdiscord.command.actions.FilterList;
import com.ibdiscord.command.actions.FilterToggle;
import com.ibdiscord.command.permission.CommandPermission;
import com.ibdiscord.command.registry.CommandRegistrar;
import com.ibdiscord.command.registry.CommandRegistry;
import com.ibdiscord.data.db.entries.GuildData;

public final class RegistrarMod implements CommandRegistrar {

    @Override
    public void register(CommandRegistry registry) {
        Command commandFilter = registry.define("filter")
                .restrict(CommandPermission.role(GuildData.MODERATOR))
                .sub(registry.sub("create")
                        .on(new FilterCreate())
                )
                .sub(registry.sub("delete")
                        .on(new FilterDelete())
                )
                .sub(registry.sub("list")
                        .on(new FilterList())
                )
                .sub(registry.sub("toggle")
                        .on(new FilterToggle())
                );
        commandFilter.on(context -> context.replySyntax(commandFilter));
    }

}
