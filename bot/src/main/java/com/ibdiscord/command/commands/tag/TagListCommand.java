package com.ibdiscord.command.commands.tag;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.DContainer;
import com.ibdiscord.data.db.entries.TagData;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Copyright 2019 Ray Clark
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
public final class TagListCommand extends Command {

    /**
     * Creates the command.
     */
    TagListCommand() {
        super("list",
                new HashSet<>(),
                CommandPermission.discord(Permission.MANAGE_CHANNEL),
                new HashSet<>());
    }

    /**
     * Lists all tags.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.white);

        Set<String> keys = DContainer.INSTANCE.getGravity().load(new TagData(context.getGuild().getId())).getKeys();

        embedBuilder.addField("List of Tags: ", String.join(", ", keys), false);
        context.reply(embedBuilder.build());
    }
}
