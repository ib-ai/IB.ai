/**
 * Copyright 2017-2019 Arraying
 *
 * This file is part of LoyalBot.
 *
 * LoyalBot is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LoyalBot is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LoyalBot. If not, see http://www.gnu.org/licenses/.
 */

package dev.vardy.command.commands;

import dev.vardy.command.Command;
import dev.vardy.command.CommandContext;
import dev.vardy.command.permissions.CommandPermission;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.OnlineStatus;

import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public final class ServerInfoCommand extends Command {

    /**
     * Creates a new command.
     */
    public ServerInfoCommand() {
        super("serverinfo",
                Set.of("guildinfo", "si"),
                CommandPermission.discord(),
                new HashSet<>()
        );
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
                .addField("Owner", guild.getOwner().getUser().getAsTag(), true)
                .addField("Creation Date", guild.getCreationTime().format(DateTimeFormatter.RFC_1123_DATE_TIME), true)
                .addField("Voice Region", guild.getRegion().getName(), true)
                .addField("# of members", itos(guild.getMembers().size()), true)
                .addField("# of Bots", ltos(guild.getMembers().stream()
                    .filter(it -> it.getUser().isBot())
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
