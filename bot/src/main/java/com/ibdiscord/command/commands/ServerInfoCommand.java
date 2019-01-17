package com.ibdiscord.command.commands;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.OnlineStatus;

import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

/**
 * Copyright 2018 Arraying
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
public final class ServerInfoCommand extends Command {

    /**
     * Creates a new command.
     */
    public ServerInfoCommand() {
        super("serverinfo", Set.of("guildinfo", "si"), CommandPermission.discord(), new HashSet<>());
    }

    /**
     * Shows server info.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        var guild = context.getGuild();
        context.reply(new EmbedBuilder()
                .addField("ID", guild.getId(), true)
                .addField("Owner", guild.getOwner().getUser().getName() + "#" + guild.getOwner().getUser().getDiscriminator(), true)
                .addField("Creation Date", guild.getCreationTime().format(DateTimeFormatter.RFC_1123_DATE_TIME), true)
                .addField("Voice Region", guild.getRegion().getName(), true)
                .addField("# of members", itos(guild.getMembers().size()), true)
                .addField("# of Bots", ltos(guild.getMembers().stream()
                    .filter(it -> !it.getUser().isBot())
                    .count()), true)
                .addField("Currently Online", ltos(guild.getMembers().stream()
                    .filter(it -> it.getOnlineStatus() != OnlineStatus.OFFLINE)
                    .count()), true)
                .addField("# of Roles", itos(guild.getRoles().size()), true)
                .addField("# of Channels", itos(guild.getVoiceChannels().size() + guild.getTextChannels().size() + guild.getCategories().size()), true)
                .build()
        );
    }

    /**
     * I am lazy. Don't judge me.
     * @param integer The int.
     * @return A string.
     */
    private String itos(Integer integer) {
        return String.valueOf(integer);
    }

    /**
     * Tribute to our comrade, longschlong.
     * @param schlong The long.
     * @return A string.
     */
    private String ltos(Long schlong) {
        return String.valueOf(schlong);
    }

}
