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
import com.ibdiscord.command.actions.*;
import com.ibdiscord.command.permission.CommandPermission;
import com.ibdiscord.command.registry.CommandRegistrar;
import com.ibdiscord.command.registry.CommandRegistry;
import net.dv8tion.jda.api.Permission;

public final class RegistrarSys implements CommandRegistrar {

    @Override
    public void register(CommandRegistry registry) {
        Command commandTag = registry.define("tag") // Explicitly state it to allow cross referencing.
                .sub(registry.sub("activate")
                        .restrict(CommandPermission.discord(Permission.MANAGE_SERVER))
                        .on(new TagActivate())
                )
                .sub(registry.sub("create")
                        .restrict(CommandPermission.discord(Permission.MANAGE_SERVER))
                        .on(new TagCreate())
                )
                .sub(registry.sub("delete")
                        .restrict(CommandPermission.discord(Permission.MANAGE_SERVER))
                        .on(new TagDelete())
                )
                .sub(registry.sub("disabled")
                        .on(new TagDisabled())
                )
                .sub(registry.sub("find")
                        .on(new TagFind())
                )
                .sub(registry.sub("list")
                        .on(new TagList())
                );
        commandTag.on(context -> context.replySyntax(commandTag));
    }

}
