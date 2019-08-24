package com.ibdiscord.command.commands.tag;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.TagData;
import com.ibdiscord.utils.UInput;
import net.dv8tion.jda.core.Permission;

import java.util.List;
import java.util.Set;

/**
 * Copyright 2017-2019 Ray Clark, Arraying
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
public final class TagDeleteCommand extends Command {

    /**
     * Creates the command.
     */
    TagDeleteCommand() {
        super("delete",
                Set.of(),
                CommandPermission.discord(Permission.MANAGE_CHANNEL),
                Set.of()
        );
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
            TagData tagData = DataContainer.INSTANCE.getGravity().load(new TagData(context.getGuild().getId()));
            tagData.unset(names.get(0));
            DataContainer.INSTANCE.getGravity().save(tagData);
            context.reply("The tag has been removed.");
        }

    }
}
