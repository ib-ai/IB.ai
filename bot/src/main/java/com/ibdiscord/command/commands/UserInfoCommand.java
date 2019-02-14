package com.ibdiscord.command.commands;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.utils.UInput;
import net.dv8tion.jda.core.EmbedBuilder;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
public final class UserInfoCommand extends Command {

    /**
     * Creates the command.
     */
    public UserInfoCommand() {
        super("userinfo",
                Set.of("memberinfo", "ui"),
                CommandPermission.discord(),
                new HashSet<>()
        );
    }

    /**
     * Shows user information.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        var target = context.getMember();
        if(context.getArguments().length >= 1) {
            target = UInput.getMember(context.getGuild(), context.getArguments()[0]);
        }
        if(target == null) {
            context.reply("User not found!");
            return;
        }
        var user = target.getUser();
        var joinPosition = context.getGuild().getMembers().stream()
                .sorted(Comparator.comparingLong(a -> a.getUser().getIdLong()))
                .collect(Collectors.toList())
                .indexOf(target) + 1;
        context.reply(new EmbedBuilder()
                .setAuthor(user.getName() + "#" + user.getDiscriminator(), "https://discord.gg/ibo", user.getEffectiveAvatarUrl())
                .addField("ID", user.getId(), true)
                .addField("Nickname", target.getEffectiveName(), true)
                .addField("Status", target.getOnlineStatus().toString(), true)
                .addField("Game", target.getGame() == null ? "N/A" : target.getGame().getName(), true)
                .addField("Joined", target.getJoinDate().format(DateTimeFormatter.RFC_1123_DATE_TIME), true)
                .addField("Join Position", String.valueOf(joinPosition), true)
                .addField("Registered", user.getCreationTime().format(DateTimeFormatter.RFC_1123_DATE_TIME), true)
                .build()
        );
    }

}
