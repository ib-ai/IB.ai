/* Copyright 2017-2019 Arraying
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

package com.ibdiscord.command.commands;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.GuildUserData;
import com.ibdiscord.utils.UInput;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.stream.Collectors;

public final class UserInfoCommand extends Command {

    /**
     * Creates the command.
     */
    public UserInfoCommand() {
        super("userinfo",
                CommandPermission.discord(),
                Set.of()
        );
    }

    /**
     * Shows user information.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        Member target = context.getMember();
        if(context.getArguments().length >= 1) {
            target = UInput.getMember(context.getGuild(), context.getArguments()[0]);
        }
        if(target == null) {
            context.reply("User not found!");
            return;
        }
        User user = target.getUser();
        int joinPosition = context.getGuild().getMembers().stream()
                .sorted((o1, o2) -> {
                    long a = o1.getTimeJoined().toInstant().toEpochMilli();
                    long b = o2.getTimeJoined().toInstant().toEpochMilli();
                    return Long.compare(a, b);
                })
                .collect(Collectors.toList())
                .indexOf(target) + 1;
        String joinOverride = DataContainer.INSTANCE.getGravity()
                .load(new GuildUserData(context.getGuild().getId(), user.getId()))
                .get("position")
                    .asString();
        Activity activity = target.getActivities().size() == 0 ? null : target.getActivities().get(0);
        context.reply(new EmbedBuilder()
                .setAuthor(user.getName() + "#" + user.getDiscriminator(), "https://discord.gg/ibo", user.getEffectiveAvatarUrl())
                .addField("ID", user.getId(), true)
                .addField("Nickname", target.getEffectiveName(), true)
                .addField("Status", target.getOnlineStatus().toString(), true)
                .addField("Game", activity == null ? "N/A" : activity.getName(), true)
                .addField("Joined", target.getTimeJoined().format(DateTimeFormatter.RFC_1123_DATE_TIME), true)
                .addField("Join Position", joinOverride == null ? String.valueOf(joinPosition) : joinOverride, true)
                .addField("Registered", user.getTimeCreated().format(DateTimeFormatter.RFC_1123_DATE_TIME), true)
                .build()
        );
    }

}
