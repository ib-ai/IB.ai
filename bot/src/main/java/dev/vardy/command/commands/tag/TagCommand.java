/**
 * Copyright 2017-2019 Ray Clark
 *
 * This file is part of LoyalBot.
 *
 * LoyalBot is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LoyalBot is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LoyalBot. If not, see http://www.gnu.org/licenses/.
 */

package dev.vardy.command.commands.tag;

import dev.vardy.command.Command;
import dev.vardy.command.CommandContext;
import dev.vardy.command.permissions.CommandPermission;
import net.dv8tion.jda.core.Permission;

import java.util.Set;

public final class TagCommand extends Command {

    /**
     * Creates the command.
     */
    public TagCommand() {
        super("tag",
                Set.of("tags"),
                CommandPermission.discord(Permission.MANAGE_CHANNEL),
                Set.of(new TagCreateCommand(), new TagDeleteCommand(), new TagListCommand())
        );
        this.correctUsage = "tag <create/delete/list> \"tag name\" \"tag value\"";
    }

    /**
     * Shows a list of sub commands.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        sendUsage(context);
    }

}
