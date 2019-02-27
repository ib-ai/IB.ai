package com.ibdiscord.command.commands.react;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.DContainer;
import com.ibdiscord.data.db.entries.ReactionData;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.HashSet;
import java.util.Set;

/**
 * Copyright 2019 Arraying
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
                new HashSet<>()
        );
    }

    /**
     * Modifies the data.
     * @param data The data.
     * @param emote The emote.
     * @param role The role.
     */
    protected abstract void modifyData(ReactionData data, Emote emote, Role role);

    /**
     * Modifies the message.
     * @param message The message.
     * @param emote The emote.
     */
    protected abstract void modifyMessage(Message message, Emote emote);

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
        long emojiId;
        long roleId;
        try {
            channelId = Long.valueOf(context.getArguments()[0]);
            messageId = Long.valueOf(context.getArguments()[1]);
            emojiId = Long.valueOf(context.getArguments()[2]);
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
        Emote emote = context.getGuild().getEmoteById(emojiId);
        if(emote == null) {
            context.reply("The emote provided does not exist.");
            return;
        }
        Role role = context.getGuild().getRoleById(roleId);
        if(role == null) {
            context.reply("The role provided does not exist.");
            return;
        }
        ReactionData data = DContainer.INSTANCE.getGravity().load(new ReactionData(context.getGuild().getId(), messageId));
        modifyData(data, emote, role);
        modifyMessage(message, emote);
        DContainer.INSTANCE.getGravity().save(data);
        context.reply("Consider it done.");
    }

}
