/**
 * Copyright 2017-2019 Arraying, Jarred Vardy <jarred.vardy@gmail.com>
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

package com.ibdiscord.command.commands.react;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.react.ReactionData;
import com.ibdiscord.localisation.Localiser;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
     * @param roleIDs The role.
     */
    protected abstract void modifyData(ReactionData data, String emote, List<String> roleIDs);

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
            context.reply(Localiser.__(context, "error.missing_channelid"));
            return;
        }
        if(context.getArguments().length < 2) {
            context.reply(Localiser.__(context, "error.missing_messageid"));
            return;
        }
        if(context.getArguments().length < 3) {
            context.reply(Localiser.__(context, "error.missing_emojiid"));
            return;
        }
        if(context.getArguments().length < 4) {
            context.reply(Localiser.__(context, "error.missing_roleid"));
            return;
        }
        long channelId;
        long messageId;
        String emoteRaw = context.getArguments()[2];
        ArrayList<String> roleArgs;
        try {
            channelId = Long.valueOf(context.getArguments()[0]);
            messageId = Long.valueOf(context.getArguments()[1]);
            roleArgs = new ArrayList<>(Arrays.asList(context.getArguments()).subList(3, context.getArguments().length));
        } catch(NumberFormatException exception) {
            context.reply(Localiser.__(context, "error.reaction_invalidid"));
            return;
        }
        TextChannel channel = context.getGuild().getTextChannelById(channelId);
        if(channel == null) {
            context.reply(Localiser.__(context, "error.reaction_channel"));
            return;
        }
        Message message;
        try {
            message = channel.getMessageById(messageId).complete();
        } catch(IllegalArgumentException exception) {
            context.reply(Localiser.__(context, "error.reaction_message"));
            return;
        }

        // Checking that all roleIDs are valid
        ArrayList<String> roleIDData = new ArrayList<>(roleArgs);
        ArrayList<Role> roles = new ArrayList<>();
        roleArgs.stream()
                .map(arg -> arg.replace("!", ""))
                .forEach(id -> roles.add(context.getGuild().getRoleById(id)));
        if(roles.isEmpty() || roles.contains(null)) {
            context.reply(Localiser.__(context, "error.reaction_role"));
            return;
        }
        ReactionData data = DataContainer.INSTANCE.getGravity().load(new ReactionData(context.getGuild().getId(), messageId));
        modifyData(data, emoteRaw, roleIDData);
        modifyMessage(message, context.getMessage().getEmotes().isEmpty() ? emoteRaw : context.getMessage().getEmotes().get(0));
        DataContainer.INSTANCE.getGravity().save(data);
        context.reply(Localiser.__(context, "success.done"));
    }

}
