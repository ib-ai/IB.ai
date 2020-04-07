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
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.RejectedExecutionException;

public final class HelperList implements CommandAction {

    private LocalConfig config = new LocalConfig();

    private final File helperAliases = new File(config.getLangBase() + "helper_aliases.json");

    /**
     * Creates list of helpers.
     * @param context The command context.
     */
    @Override
    public void accept(CommandContext context) {
        String channel = context.getChannel().getId();
        String desiredRole;
        if (context.getArguments().length == 1) {
            desiredRole = context.getArguments()[0];
            System.out.println(context.getArguments()[0]);

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
                    context.replyI18n("error.generic_arg_length");
                }
                Role role = context.getGuild().getRoleById(aliasKey.toString());
                Message message = context.getMessage().getTextChannel()
                        .sendMessage(UEmbed.helperMessageEmbed(context.getGuild(), role))
                        .complete();
            } catch (RejectedExecutionException | IOException e) {
                context.replyI18n("error.generic");
            }
        }
    }
}
