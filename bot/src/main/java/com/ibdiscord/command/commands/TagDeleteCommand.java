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
import com.ibdiscord.data.db.entries.BotPrefixData;
import com.ibdiscord.data.db.entries.TagData;
import com.ibdiscord.main.IBai;

import net.dv8tion.jda.core.Permission;

import java.util.HashSet;

public final class TagDeleteCommand extends Command {

    public TagDeleteCommand() {
        super("delete",
                new HashSet<String>(),
                CommandPermission.discord(Permission.MANAGE_CHANNEL),
                new HashSet<Command>());
    }

    @Override
    protected void execute(CommandContext context) {

        if (context.getArguments().length == 0) {
            String botPrefix = IBai.getConfig().getStaticPrefix();
            try {
                botPrefix = DContainer.getGravity().load(new BotPrefixData(context.getGuild().getId())).get().toString();
            } catch(Exception e) {
                e.printStackTrace();
            }
            context.reply("Correct usage: `" + botPrefix + "tag delete \"[trigger]\"`");
        } else {
            StringBuilder triggerBuilder = new StringBuilder();
            for(String message : context.getArguments()) {
                triggerBuilder.append(message);
            }
            String trigger = triggerBuilder.toString().split("\"")[1];
            try {
                TagData tags = DContainer.getGravity().load(new TagData(context.getGuild().getId()));
                tags.unset(trigger);
                DContainer.getGravity().save(tags);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
