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
import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.GuildData;

import com.ibdiscord.data.db.entries.HelperMessageData;
import com.ibdiscord.utils.UEmbed;
import com.ibdiscord.utils.UInput;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.concurrent.RejectedExecutionException;

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

        registry.define("helpermessage")
                .restrict(CommandPermission.discord(Permission.MANAGE_ROLES))
                .on(context -> {
                    context.assertArguments(2, "error.generic_arg_length");
                    if (context.getMessage().getMentionedChannels().size() < 1) {
                        context.replyI18n("error.missing_channel");
                        return;
                    }
                    TextChannel channel = context.getMessage().getMentionedChannels().get(0);
                    Role role = UInput.getRole(context.getGuild(), context.getArguments()[0]);
                    if (role == null) {
                        context.replyI18n("error.missing_roleid");
                        return;
                    }

                    HelperMessageData helperMessageData = DataContainer.INSTANCE.getGravity().load(
                            new HelperMessageData(context.getGuild().getId())
                    );

                    if (helperMessageData.getKeys().contains(role.getId())) {
                        String[] ids = helperMessageData.get(role.getId()).asString().split(",");
                        if (ids.length == 2) {
                            TextChannel channelOld = UInput.getChannel(context.getGuild(), ids[0]);
                            if (channelOld != null) {
                                channelOld.deleteMessageById(ids[1]).queue();
                            }
                        }
                    }

                    try {
                        Message message = channel.sendMessage(UEmbed.helperMessageEmbed(context.getGuild(), role))
                                .complete();
                        helperMessageData.set(role.getId(), String.format("%s,%s", channel.getId(), message.getId()));
                        DataContainer.INSTANCE.getGravity().save(helperMessageData);
                        message.pin().queue();
                    } catch (RejectedExecutionException e) {
                        context.replyI18n("error.pin_channel");
                        return;
                    }
                });

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
