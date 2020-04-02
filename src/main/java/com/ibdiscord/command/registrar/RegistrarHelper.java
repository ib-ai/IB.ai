/* Copyright 2018-2020 Jarred Vardy
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

import com.ibdiscord.command.actions.Pin;
import com.ibdiscord.command.actions.Roleing;
import com.ibdiscord.command.permission.CommandPermission;
import com.ibdiscord.command.registry.CommandRegistrar;
import com.ibdiscord.command.registry.CommandRegistry;
import com.ibdiscord.data.db.entries.GuildData;

import net.dv8tion.jda.api.Permission;

public final class RegistrarHelper implements CommandRegistrar {

    /**
     * Registers commands.
     * @param registry The command registry.
     */
    @Override
    public void register(CommandRegistry registry) {
        registry.define("helper")
                .restrict(CommandPermission.discord(Permission.MANAGE_SERVER))
                .on(new Roleing(GuildData.HELPER, "helper_permission"));

        registry.define("pin")
                .restrict(CommandPermission.role(GuildData.HELPER))
                .on(new Pin());

        registry.define("unpin")
                .restrict(CommandPermission.role(GuildData.HELPER))
                .on(context -> {
                    context.assertArguments(1, "error.generic_arg_length");
                    long id = context.assertLong(context.getArguments()[0],
                            null,
                            null,
                            "error.pin_channel");

                    context.getChannel().unpinMessageById(id).queue(success ->
                                    context.replyI18n("success.done"),
                        error ->
                                    context.replyI18n("error.pin_channel")
                    );
                });
    }

}
