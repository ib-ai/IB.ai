/* Copyright 2017-2019 Arraying
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

package com.ibdiscord.command.commands;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.utils.UInput;
import com.ibdiscord.utils.UString;
import net.dv8tion.jda.api.entities.Member;

import java.util.Set;

public final class AvatarCommand extends Command {

    /**
     * Creates the command.
     */
    public AvatarCommand() {
        super("avatar",
                Set.of("av"),
                CommandPermission.discord(),
                Set.of()
        );
    }

    /**
     * Sends the avatar of a user.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        Member target;
        if(context.getArguments().length < 1) {
            target = context.getMember();
        } else {
            target = UInput.getMember(context.getGuild(), UString.concat(context.getArguments(), " ", 0));
        }
        if(target == null) {
            context.reply("Unknown user.");
            return;
        }
        context.reply(target.getUser().getEffectiveAvatarUrl() + "?size=1024");
    }

}
