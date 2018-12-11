package com.ibdiscord.command.commands;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.main.IBai;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;

import java.awt.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        EmbedBuilder ebHelpMenu = new EmbedBuilder();
        ebHelpMenu.setColor(Color.white);
        ebHelpMenu.setAuthor("IB.ai", "https://discord.me/pbh", null);
        ebHelpMenu.setDescription("Hey! Welcome to the IBO Discord Server. I'm IB.ai version: `" + botPrefix + "` Here's some of the things I can do:");
        //TODO: Write help message
        context.reply(ebHelpMenu.build());
    }
}
