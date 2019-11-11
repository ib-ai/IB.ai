/* Copyright 2019 Arraying
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibdiscord.command.commands;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.GuildData;
import com.ibdiscord.utils.UString;
import de.arraying.gravity.Gravity;
import net.dv8tion.jda.api.Permission;

import java.util.Set;


public final class HelperCommand extends Command {

    /**
     * Creates a new command.
     */
    public HelperCommand() {
        super("helper",
                CommandPermission.discord(Permission.MANAGE_SERVER),
                Set.of()
        );
        this.correctUsage = "helper [new role]";
    }

    /**
     * Executes the command.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        Gravity gravity = DataContainer.INSTANCE.getGravity();
        GuildData guildData = gravity.load(new GuildData(context.getGuild().getId()));
        if(context.getArguments().length == 0) {
            String permission = guildData.get(GuildData.HELPER)
                    .defaulting("not set")
                    .asString();
            context.reply(__(context, "info.helper_permission", permission));
            return;
        }
        String newValue = UString.concat(context.getArguments(), " ", 0);
        guildData.set(GuildData.HELPER, newValue);
        gravity.save(guildData);
        context.reply(__(context, "success.helper_permission"));
    }

}
