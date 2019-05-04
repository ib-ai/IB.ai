/**
 * Copyright 2017-2019 Arraying, Ray Clark
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

import dev.vardy.LoyalBot;
import dev.vardy.command.Command;
import dev.vardy.command.CommandContext;
import dev.vardy.command.permissions.CommandPermission;
import dev.vardy.utils.UDatabase;

import net.dv8tion.jda.core.EmbedBuilder;

import java.awt.*;
import java.util.HashSet;
import java.util.stream.Collectors;

public final class HelpCommand extends Command {

    /**
     * Creates the command.
     */
    public HelpCommand() {
        super("help",
                new HashSet<>(),
                CommandPermission.discord(),
                new HashSet<>()
        );
    }

    /**
     * Shows a list of available commands.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        String botPrefix = UDatabase.getPrefix(context.getGuild());

        EmbedBuilder ebHelpMenu = new EmbedBuilder();
        ebHelpMenu.setColor(Color.white);
        ebHelpMenu.setAuthor("LoyalBot", "https://jarredvardy.com/", null);
        ebHelpMenu.setDescription("Hey! I'm LoyalBot version: `" + LoyalBot.INSTANCE.getConfig().getBotVersion() + "`. " +
                "All command arguments in <> are required, [] are optional.");
        ebHelpMenu.addField("Some Things I Can Do:", "- Show user information about a specific user: `" + botPrefix + "userinfo [user]`\n" +
                "- Show information about the server `" + botPrefix + "serverinfo`\n" +
                "- Check if the bot is responding: `" + botPrefix + "ping`\n", false);
        ebHelpMenu.setFooter("Developed with <3", null);
        context.reply(ebHelpMenu.build());
    }

}
