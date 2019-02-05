package com.ibdiscord.command.commands;

import com.ibdiscord.IBai;
import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.utils.UDatabase;
import net.dv8tion.jda.core.EmbedBuilder;

import java.awt.*;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * Copyright 2019 Arraying, Ray Clark
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
        ebHelpMenu.setAuthor("IB.ai", "https://discord.me/pbh", null);
        ebHelpMenu.setDescription("Hey! Welcome to the IBO Discord Server. I'm IB.ai version: `" + IBai.INSTANCE.getConfig().getBotVersion() + "`. " +
                "All command arguments in <> are required, [] are optional.");
        ebHelpMenu.addField("Getting Started:", "Grab a year role by clicking the appropriate emote in <#517896170581000224>. " +
                "You can also add yourself to certain subjects this way. Share your passions with others by joining the lounges of certain interests, also using " +
                "reactions, in <#504452978699272192>.", false);
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
