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
import com.ibdiscord.data.db.entries.BotPrefixData;
import com.ibdiscord.main.IBai;

import net.dv8tion.jda.core.EmbedBuilder;

import java.awt.*;
import java.util.HashSet;

public class HelpCommand extends Command {

    public HelpCommand() {
        super("help",
                new HashSet<>(),
                CommandPermission.discord(),
                new HashSet<>());
    }
    @Override
    protected void execute(CommandContext context) {
        String botPrefix = IBai.getConfig().getStaticPrefix();
        try {
            botPrefix = IBai.getDatabase().getGravity().load(new BotPrefixData(context.getGuild().getId())).get().toString();
        } catch(Exception e){
        }

        EmbedBuilder ebHelpMenu = new EmbedBuilder();
        ebHelpMenu.setColor(Color.white);
        ebHelpMenu.setAuthor("IB.ai", "https://discord.me/pbh", null);
        ebHelpMenu.setDescription("Hey! Welcome to the IBO Discord Server. I'm IB.ai version: `" + botPrefix + "` Here's some of the things I can do:");
        //TODO: Write help message
        context.reply(ebHelpMenu.build());
    }
}
