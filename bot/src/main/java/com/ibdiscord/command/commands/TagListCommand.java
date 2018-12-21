/**
 * Copyright 2018 raynichc
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

/**
 * @author raynichc
 * @since 2018.11.29
 */

package com.ibdiscord.command.commands;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.DContainer;
import com.ibdiscord.data.db.entries.TagData;
import com.ibdiscord.main.IBai;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public final class TagListCommand extends Command {

    public TagListCommand() {
        super("list",
                new HashSet<String>(),
                CommandPermission.discord(Permission.MANAGE_CHANNEL),
                new HashSet<Command>());
    }

    @Override
    protected void execute(CommandContext context) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.white);

        StringBuilder stringBuilder = new StringBuilder();

        try {
            Set<String> keys = DContainer.getGravity().load(new TagData(context.getGuild().getId())).getKeys();
            for (String key : keys) {
                stringBuilder.append(stringBuilder.length() == 0 ? "" :  ", ").append(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        embedBuilder.addField("List of Tags:", stringBuilder.toString(), false);

        context.reply(embedBuilder.build());
    }
}
