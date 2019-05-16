package com.ibdiscord.command.commands.react;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.DContainer;
import com.ibdiscord.data.db.entries.ReactionData;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;

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
public abstract class ReactionManageCommand extends Command {

    /**
     * Creates a new command.
     * @param name The name of the command, all lowercase.
     * @param aliases Any aliases the command has, also all lowercase.
     */
    ReactionManageCommand(String name, Set<String> aliases) {
        super(name,
                aliases,
                CommandPermission.discord(Permission.MANAGE_ROLES),
                Set.of()
        );
    }

    /**
     * Modifies the data.
     * @param data The data.
     * @param emote The emote.
     * @param role The role.
     */
    protected abstract void modifyData(ReactionData data, String emote, Role role);

    /**
     * Modifies the message.
     * @param message The message.
     * @param emote The emote.
     */
    protected abstract void modifyMessage(Message message, Object emote);

    /**
     * Executes the command.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        if(context.getArguments().length < 1) {
            context.reply("Please provide the channel ID.");
            return;
        }
        if(context.getArguments().length < 2) {
            context.reply("Please provide the message ID.");
            return;
        }
        if(context.getArguments().length < 3) {
            context.reply("Please provide the emoji ID.");
            return;
        }
        if(context.getArguments().length < 4) {
            context.reply("Please provide the role ID.");
            return;
        }
        long channelId;
        long messageId;
        String emoteRaw = context.getArguments()[2];
        long roleId;
        try {
            channelId = Long.valueOf(context.getArguments()[0]);
            messageId = Long.valueOf(context.getArguments()[1]);
            roleId = Long.valueOf(context.getArguments()[3]);
        } catch(NumberFormatException exception) {
            context.reply("One of your IDs is not a number. Please make sure you only use numeric IDs, and not mentions.");
            return;
        }
        TextChannel channel = context.getGuild().getTextChannelById(channelId);
        if(channel == null) {
            context.reply("The channel provided does not exist.");
            return;
        }
        Message message;
        try {
            message = channel.getMessageById(messageId).complete();
        } catch(IllegalArgumentException exception) {
            context.reply("The message provided does not exist.");
            return;
        }
        Role role = context.getGuild().getRoleById(roleId);
        if(role == null) {
            context.reply("The role provided does not exist.");
            return;
        }
        ReactionData data = DContainer.INSTANCE.getGravity().load(new ReactionData(context.getGuild().getId(), messageId));
        modifyData(data, emoteRaw, role);
        modifyMessage(message, context.getMessage().getEmotes().isEmpty() ? emoteRaw : context.getMessage().getEmotes().get(0));
        DContainer.INSTANCE.getGravity().save(data);
        context.reply("Consider it done.");
    }

}
