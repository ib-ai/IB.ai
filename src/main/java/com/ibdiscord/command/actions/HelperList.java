/* Copyright 2020 Nathaneal Varghese
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

package com.ibdiscord.command.actions;

import com.ibdiscord.command.CommandAction;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.data.LocalConfig;
import com.ibdiscord.utils.UEmbed;
import com.ibdiscord.utils.UJSON;
import de.arraying.kotys.JSON;
import de.arraying.kotys.JSONArray;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

public final class HelperList implements CommandAction {

    private LocalConfig config = new LocalConfig();

    private final File helperAliases = new File(config.getLangBase() + "helper_aliases.json");

    /**
     * Creates list of helpers.
     * @param context The command context.
     */
    @Override
    public void accept(CommandContext context) {
        MessageChannel channelMessage = context.getChannel();
        String desiredRole;
        if (context.getArguments().length < 1) {

            List<Role> allRoles = context.getGuild().getRoles();
            AtomicBoolean roleInChannel = new AtomicBoolean(false);

            allRoles.forEach(role -> {
                if (role.hasPermission((GuildChannel) channelMessage, Permission.MESSAGE_MANAGE)) {
                    String[] words = role.getName().split(" ");
                    for (int i = 0; i < words.length; i++) {
                        if ((words[i].toLowerCase().equals(" helper") || words[i].toLowerCase().equals("helper"))
                                && i == 1) {
                            roleInChannel.set(true);

                            Role roleFinal = context.getGuild().getRoleById(role.getId());
                            Message message = context.getMessage().getTextChannel()
                                    .sendMessage(UEmbed.helperMessageEmbed(context.getGuild(), roleFinal))
                                    .complete();
                        }
                    }
                }
            });

            if (!roleInChannel.get()) {
                context.replyI18n("error.helper_list_channel");
            }
        }

        if (context.getArguments().length == 1) {
            desiredRole = context.getArguments()[0];

            try {
                JSON json = UJSON.retrieveJSONFromFile(helperAliases.getPath());
                String aliasKey = json.raw().keySet().stream().filter(key -> {
                    JSONArray aliases = json.array(key);
                    for(int i = 0; i < aliases.length(); i++) {
                        String alias = aliases.string(i);
                        if(alias.equals(desiredRole.toLowerCase())) {
                            return true;
                        }
                    }
                    return false;
                }).findFirst().orElse(null);

                if (aliasKey == null) {
                    context.replyI18n("error.helper_list_incorrect");
                    return;
                }
                Role role = context.getGuild().getRoleById(aliasKey);
                Message message = context.getMessage().getTextChannel()
                        .sendMessage(UEmbed.helperMessageEmbed(context.getGuild(), role))
                        .complete();
            } catch (RejectedExecutionException | IOException e) {
                context.replyI18n("error.generic");
            }
        }
    }
}
