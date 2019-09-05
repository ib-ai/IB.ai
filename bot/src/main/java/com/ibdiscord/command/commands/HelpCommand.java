package com.ibdiscord.command.commands;

import com.ibdiscord.IBai;
import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.localisation.Localiser;
import com.ibdiscord.utils.UDatabase;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;

import java.awt.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Copyright 2017-2019 Arraying, Ray Clark
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
public final class HelpCommand extends Command {

    /**
     * Creates the command.
     */
    public HelpCommand() {
        super("help",
                Set.of(),
                CommandPermission.discord(),
                Set.of()
        );
    }

    /**
     * Shows a list of available commands.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        String botPrefix = UDatabase.getPrefix(context.getGuild());
        List<TextChannel> channelsGetRoles = context.getGuild().getTextChannelsByName("get-roles", true);
        String getRoles = channelsGetRoles.isEmpty() ? "#get-roles" : channelsGetRoles.get(0).getAsMention();
        List<TextChannel> channelsJoinLounge = context.getGuild().getTextChannelsByName("join-lounge", true);
        String joinLounge = channelsJoinLounge.isEmpty() ? "#join-lounge" : channelsJoinLounge.get(0).getAsMention();
        EmbedBuilder ebHelpMenu = new EmbedBuilder();
        ebHelpMenu.setColor(Color.white);
        ebHelpMenu.setAuthor("IB.ai", "https://discord.me/pbh", null);
        ebHelpMenu.setDescription(Localiser.__(context, "info.intro_welcome", IBai.INSTANCE.getConfig().getBotVersion()));
        ebHelpMenu.addField(Localiser.__(context, "info.intro_started1"), Localiser.__(context, "info.intro_started2", getRoles, joinLounge), false);
        ebHelpMenu.addField(Localiser.__(context, "info.features1"), Localiser.__(context, "info_features2", botPrefix), false);
        ebHelpMenu.addField(Localiser.__(context, "info.intro_dev1"), Localiser.__(context, "info.intro_dev2", IBai.INSTANCE.getConfig().getGithubLink(),
                IBai.INSTANCE.getConfig().getBotAuthors().stream()
                        .map(it -> "- " + it)
                        .collect(Collectors.joining("\n"))), false
        );
        ebHelpMenu.setFooter(Localiser.__(context, "info.intro_footer"), null);
        context.reply(ebHelpMenu.build());
    }

}
