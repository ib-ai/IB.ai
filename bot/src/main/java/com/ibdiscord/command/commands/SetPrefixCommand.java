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
 * @since 2018.12.19
 */

package com.ibdiscord.command.commands;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.DContainer;
import com.ibdiscord.data.db.entries.BotPrefixData;
import com.ibdiscord.main.IBai;

import net.dv8tion.jda.core.Permission;

import java.util.Arrays;
import java.util.HashSet;

public final class SetPrefixCommand extends Command {

    public SetPrefixCommand() {
        super("setprefix",
                new HashSet<>(),
                CommandPermission.discord(Permission.MANAGE_SERVER),
                new HashSet<>());
    }
    
    @Override
    protected void execute(CommandContext context) {
        String botPrefix = IBai.getConfig().getStaticPrefix();
        try {
            botPrefix = DContainer.getGravity().load(new BotPrefixData(context.getGuild().getId())).get().toString();
        } catch(Exception e){
            e.printStackTrace();
        }
        if(context.getArguments().length != 1) {
            context.reply("Correct usage: `" + botPrefix + "SetPrefix [Prefix]`");
            return;
        }

        String prefix = context.getArguments()[0];
        if(Arrays.stream(new String[]{"/", "$", "#", "+", "*", "?"}).anyMatch(prefix::equals)) {
            context.reply("Invalid Prefix ( " +  prefix + ")." );
            return;
        }

        try {
            BotPrefixData botPrefixData = new BotPrefixData(context.getGuild().getId());
            botPrefixData.set(prefix);
            DContainer.getGravity().save(botPrefixData);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
