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
import com.ibdiscord.data.db.entries.GuildData;
import com.ibdiscord.punish.Punishment;
import com.ibdiscord.punish.PunishmentHandler;
import com.ibdiscord.punish.PunishmentType;
import com.ibdiscord.utils.UFormatter;
import com.ibdiscord.utils.UInput;
import com.ibdiscord.utils.UString;
import net.dv8tion.jda.api.entities.Member;

import java.util.Set;

public final class WarnCommand extends Command {

    /**
     * Creates the command.
     */
    public WarnCommand() {
        super("warn",
                CommandPermission.role(GuildData.MODERATOR),
                Set.of()
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
            context.reply(__(context, "error.user_404"));
            return;
        }
        member.getUser().openPrivateChannel().queue(channel ->
                channel.sendMessage(__(context, "info.warned_you", context.getGuild().getName(), reason))
                        .queue(ignored -> {
                                    context.reply(__(context, "info.warned_they"));
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
                                    context.reply(__(context, "error.warn_send"));
                                    errorSend.printStackTrace();
                                }), errorOpen -> {
            context.reply(__(context, "error.warn_channel"));
            errorOpen.printStackTrace();
        });
    }
}
