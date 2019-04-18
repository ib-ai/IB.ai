package com.ibdiscord.command.commands.tag;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.DContainer;
import com.ibdiscord.data.db.entries.TagData;
import com.ibdiscord.utils.UInput;
import com.ibdiscord.utils.UString;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.util.HashSet;
import java.util.List;

/**
 * Copyright 2019 Ray Clark, Arraying
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public final class TagCreateCommand extends Command {

    /**
     * Creates the command.
     */
    TagCreateCommand() {
        super("create",
                new HashSet<>(),
                CommandPermission.discord(Permission.MANAGE_CHANNEL),
                new HashSet<>());
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
        TagData tags = DContainer.INSTANCE.getGravity().load(new TagData(context.getGuild().getId()));
        tags.set(trigger, output);
        DContainer.INSTANCE.getGravity().save(tags);
        context.reply("Consider it done: `%s` -> `%s`.", trigger, output);
    }

}
