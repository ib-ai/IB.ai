package com.ibdiscord.command.commands;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.entries.GuildData;
import com.ibdiscord.punish.Punishment;
import com.ibdiscord.punish.PunishmentHandler;
import com.ibdiscord.punish.PunishmentType;
import com.ibdiscord.utils.UFormatter;
import com.ibdiscord.utils.UInput;
import com.ibdiscord.utils.UString;
import net.dv8tion.jda.core.entities.Member;

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
public final class WarnCommand extends Command {

    /**
     * Creates the command.
     */
    public WarnCommand() {
        super("warn",
                Set.of(),
                CommandPermission.role(GuildData.MODERATOR),
                new HashSet<>()
        );
        this.correctUsage = "warn <user> <reason>";
    }

    /**
     * Warns a member.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        if(context.getArguments().length < 2) {
            sendUsage(context);
            return;
        }
        Member member = UInput.getMember(context.getGuild(), context.getArguments()[0]);
        String reason = UString.concat(context.getArguments(), " ", 1);
        if(member == null) {
            context.reply("User not found!");
            return;
        }
        member.getUser().openPrivateChannel().queue(channel ->
                channel.sendMessage(String.format("You have been warned on %s for: %s", context.getGuild().getName(), reason))
                .queue(ignored -> {
                            context.reply("The user has been warned.");
                            PunishmentHandler punishmentHandler = new PunishmentHandler(context.getGuild(), new Punishment(PunishmentType.WARN,
                                    UFormatter.formatMember(member.getUser()),
                                    member.getUser().getId(),
                                    UFormatter.formatMember(context.getMember().getUser()),
                                    context.getMember().getUser().getId(),
                                    reason,
                                    false
                            ));
                            punishmentHandler.onPunish();
                        },
                    errorSend -> {
                        context.reply("Could not send warning message, please attempt to do so manually.");
                        errorSend.printStackTrace();
                }), errorOpen -> {
            context.reply("Could not open private channel.");
            errorOpen.printStackTrace();
        });
    }

}
