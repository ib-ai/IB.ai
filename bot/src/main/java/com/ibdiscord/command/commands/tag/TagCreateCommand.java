package com.ibdiscord.command.commands.tag;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.TagData;
import com.ibdiscord.utils.UInput;
import com.ibdiscord.utils.UString;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;

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
public final class TagCreateCommand extends Command {

    /**
     * Creates the command.
     */
    TagCreateCommand() {
        super("create",
                Set.of(),
                CommandPermission.discord(Permission.MANAGE_CHANNEL),
                Set.of()
        );
        this.correctUsage = "tag create \"tag name\" \"tag value\"";
    }

    /**
     * Creates a tag.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        if(context.getArguments().length == 0) {
            sendUsage(context);
            return;
        }
        List<String> data = UInput.extractQuotedStrings(context.getArguments());
        if(data.size() < 2) {
            context.reply("Invalid input. Make sure to put both the trigger and output in double quotes.");
            return;
        }
        String trigger = data.get(0);
        if(UString.escapeFormatting(trigger).length()> MessageEmbed.TITLE_MAX_LENGTH) {
            context.reply("Tag name too long.");
            return;
        }
        if(!UInput.isValidRegex(trigger)) {
            context.reply("Trigger is not a valid regular expression.");
            return;
        }
        String output = data.get(1);
        if(UString.escapeFormatting(output).length() > 512) {
            context.reply("Tag value too long.");
            return;
        }
        TagData tags = DataContainer.INSTANCE.getGravity().load(new TagData(context.getGuild().getId()));
        tags.set(trigger, output);
        DataContainer.INSTANCE.getGravity().save(tags);
        context.reply("Consider it done: `%s` -> `%s`.", trigger, output);
    }

}
