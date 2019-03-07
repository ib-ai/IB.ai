package com.ibdiscord.command.commands.abstracted;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.DContainer;
import com.ibdiscord.data.db.entries.GuildData;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.Set;

/**
 * Copyright 2019 Arraying
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
public abstract class LoggingCommand extends Command {

    /**
     * A disabled log.
     */
    private static final long DISABLED = 0L;

    private final String key;

    /**
     * Creates a new command.
     * @param name The name of the command, all lowercase.
     * @param aliases Any aliases the command has, also all lowercase.
     * @param permission The permission required to execute the command.
     * @param subCommands Any sub commands the command has.
     * @param key The key for the channel field.
     */
    protected LoggingCommand(String name, Set<String> aliases, CommandPermission permission, Set<Command> subCommands, String key) {
        super(name, aliases, permission, subCommands);
        this.key = key;
    }

    /**
     * Sets or disables the logging channel.
     * @param context The command context.
     */
    @Override
    protected final void execute(CommandContext context) {
        TextChannel channel;
        if(context.getMessage().getMentionedChannels().isEmpty()) {
            channel = null;
        } else {
            channel = context.getMessage().getMentionedChannels().get(0);
        }
        GuildData guildData = DContainer.INSTANCE.getGravity().load(new GuildData(context.getGuild().getId()));
        guildData.set(key, channel == null ? DISABLED : channel.getId());
        DContainer.INSTANCE.getGravity().save(guildData);
        context.reply("The channel has been set to: " + (channel == null ? "nothing" : channel.getAsMention()) + ".");
    }

}
