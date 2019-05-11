package com.ibdiscord.input.embed;

import com.ibdiscord.input.Input;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

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
public final @AllArgsConstructor class EmbedChannelInput implements Input {

    private final EmbedBuilder builder;


    /**
     * @return null.
     */
    @Override
    public Input getSuccessor() {
        return null;
    }

    /**
     * @return 30 seconds.
     */
    @Override
    public long getTimeout() {
        return 30 * 1000;
    }

    /**
     * Sends the message.
     * @param message The initial message.
     */
    @Override
    public void initialize(Message message) {
        message.getChannel().sendMessage("What channel should the embed be sent in?").queue();
    }

    /**
     * Handles the input.
     * @param message The message.
     * @return True if the channel is accepted, false otherwise.
     */
    @Override
    public boolean offer(Message message) {
        if(message.getMentionedChannels().isEmpty()) {
            message.getChannel().sendMessage("You need to mention a channel!").queue();
            return false;
        }
        TextChannel target = message.getMentionedChannels().get(0);
        if(!message.getMember().hasPermission(target, Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS)) {
            message.getChannel().sendMessage("You do not have access to that channel.").queue();
            return false;
        }
        target.sendMessage(builder.build()).queue(success ->
                        message.getChannel().sendMessage("Done.").queue(),
                failure -> message.getChannel().sendMessage("Failed to send, are the required perms present?").queue()
        );
        return true;
    }

}
