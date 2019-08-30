package com.ibdiscord.command.commands;

import com.ibdiscord.IBai;
import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.utils.UDatabase;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

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
        ebHelpMenu.setDescription("Hey! Welcome to the IBO Discord Server. I'm IB.ai version: `" + IBai.INSTANCE.getConfig().getBotVersion() + "`. " +
                "All command arguments in <> are required, [] are optional.");
        ebHelpMenu.addField("Getting Started:", "Grab a year role by clicking the appropriate emote in " + getRoles + ". " +
                "You can also add yourself to certain subjects this way. Share your passions with others by joining the lounges of certain interests, also using " +
                "reactions, in " + joinLounge + ".", false);
        ebHelpMenu.addField("Some Things I Can Do:", "- To get a link to up-to-date downloads to textbooks, papers, guides and more, type `link the resources` in chat\n" +
                "- Show user information about a specific user: `" + botPrefix + "userinfo [user]`\n" +
                "- Show information about the server `" + botPrefix + "serverinfo`\n" +
                "- Check if the bot is responding: `" + botPrefix + "ping`\n", false);
        ebHelpMenu.addField("Development:", "IB.ai is a [FOSS](" + IBai.INSTANCE.getConfig().getGithubLink() + ") project developed the community.\n" +
                "Credit to the following minions for developing me:\n" + IBai.INSTANCE.getConfig().getBotAuthors().stream()
                        .map(it -> "- " + it)
                        .collect(Collectors.joining("\n")) + "\n\n" +
                "Please consider [donating](https://paypal.me/libreresources) if you appreciate their efforts. " +
                "Donations go directly back into the bot and other server projects, paying for server fees", false
        );
        ebHelpMenu.setFooter("Developed with <3", null);
        context.reply(ebHelpMenu.build());
    }

}
