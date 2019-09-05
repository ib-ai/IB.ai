package com.ibdiscord.command.commands;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.localisation.Localiser;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Guild;

import java.time.format.DateTimeFormatter;
import java.util.Set;

/**
 * Copyright 2017-2019 Arraying
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
public final class ServerInfoCommand extends Command {

    /**
     * Creates a new command.
     */
    public ServerInfoCommand() {
        super("serverinfo",
                Set.of("guildinfo", "si"),
                CommandPermission.discord(),
                Set.of()
        );
    }

    /**
     * Shows server info.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        Guild guild = context.getGuild();
        context.reply(new EmbedBuilder()
                .addField("ID", guild.getId(), true)
                .addField(Localiser.__(context, "info.owner"), guild.getOwner().getUser().getAsTag(), true)
                .addField(Localiser.__(context, "info.creation_date"), guild.getCreationTime().format(DateTimeFormatter.RFC_1123_DATE_TIME), true)
                .addField(Localiser.__(context, "info.vc_region"), guild.getRegion().getName(), true)
                .addField(Localiser.__(context, "info.number_members"), itos(guild.getMembers().size()), true)
                .addField(Localiser.__(context, "info.number_bots"), ltos(guild.getMembers().stream()
                    .filter(it -> it.getUser().isBot())
                    .count()), true)
                .addField(Localiser.__(context, "info.number_online"), ltos(guild.getMembers().stream()
                    .filter(it -> it.getOnlineStatus() != OnlineStatus.OFFLINE)
                    .count()), true)
                .addField(Localiser.__(context, "info.number_roles"), itos(guild.getRoles().size()), true)
                .addField(Localiser.__(context, "info.number_channels"), itos(guild.getVoiceChannels().size() + guild.getTextChannels().size() + guild.getCategories().size()), true)
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
