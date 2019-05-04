/**
 * Copyright 2017-2019 Ray Clark, Arraying
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
import dev.vardy.data.db.DContainer;
import dev.vardy.data.db.entries.TagData;
import dev.vardy.utils.UInput;
import net.dv8tion.jda.core.Permission;

import java.util.HashSet;
import java.util.List;

public final class TagDeleteCommand extends Command {

    /**
     * Creates the command.
     */
    TagDeleteCommand() {
        super("delete",
                new HashSet<>(),
                CommandPermission.discord(Permission.MANAGE_CHANNEL),
                new HashSet<>());
        this.correctUsage = "tag delete \"tag name\"";
    }

    /**
     * Deletes a tag.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        if(context.getArguments().length == 0) {
            sendUsage(context);
        } else {
            List<String> names = UInput.extractQuotedStrings(context.getArguments());
            if(names.isEmpty()) {
                context.reply("Invalid name, please provide it in quotation marks.");
                return;
            }
            TagData tagData = DContainer.INSTANCE.getGravity().load(new TagData(context.getGuild().getId()));
            tagData.unset(names.get(0));
            DContainer.INSTANCE.getGravity().save(tagData);
            context.reply("The tag has been removed.");
        }

    }
}
