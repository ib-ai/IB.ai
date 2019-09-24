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

import com.ibdiscord.IBai;
import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.GuildData;
import com.ibdiscord.data.db.entries.punish.PunishmentData;
import com.ibdiscord.data.db.entries.punish.PunishmentsData;
import com.ibdiscord.punish.Punishment;
import com.ibdiscord.punish.PunishmentHandler;
import com.ibdiscord.utils.UString;
import de.arraying.gravity.Gravity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Set;

import static com.ibdiscord.data.db.entries.punish.PunishmentData.*;

public final class ReasonCommand extends Command {

    /**
     * Creates the command.
     */
    public ReasonCommand() {
        super("reason",
                CommandPermission.role(GuildData.MODERATOR),
                Set.of()
        );
        this.correctUsage = "reason <case ID> <reason>";
    }

    /**
     * Sets the reason.
     * @param context The context.
     */
    @Override
    protected void execute(CommandContext context) {
        if(context.getArguments().length < 2) {
            sendUsage(context);
            return;
        }
        Guild guild = context.getGuild();
        String caseNumber = context.getArguments()[0];
        String reason = UString.concat(context.getArguments(), " ", 1);
        Gravity gravity = DataContainer.INSTANCE.getGravity();
        PunishmentsData punishmentList = gravity.load(new PunishmentsData(guild.getId()));
        if(!punishmentList.contains(caseNumber)) {
            context.reply(__(context, "error.lookup_noexist"));
            return;
        }
        Long caseId = UString.toLong(caseNumber);
        if(caseId == null) {
            context.reply(__(context, "error.lookup_convert"));
            return;
        }
        Punishment punishment = Punishment.of(context.getGuild(), caseId);
        punishment.setReason(reason);
        PunishmentData punishmentData = gravity.load(new PunishmentData(guild.getId(), caseId));
        PunishmentHandler punishmentHandler = new PunishmentHandler(guild, punishment);
        TextChannel channel = punishmentHandler.getLogChannel();
        if(channel == null) {
            context.reply(__(context, "error.reason_logging"));
            return;
        }
        boolean redacted = context.getOptions().stream()
                .anyMatch(it -> it.getName().equalsIgnoreCase("redacted") || it.getName()
                        .equalsIgnoreCase("redact"));
        IBai.INSTANCE.getLogger().info("Redacting: " + redacted);
        punishmentData.set(REASON, reason);
        punishmentData.set(REDACTED, redacted);
        punishment.redacting(redacted);
        gravity.save(punishmentData);
        channel.editMessageById(punishmentData.get(MESSAGE).defaulting(0L).asLong(),
            punishment.getLogPunishment(guild, caseId)).queue(
                outstandingMove -> {
                    punishmentData.set(REASON, reason);
                    context.reply(__(context, "success.reason"));
                },
                error -> {
                    context.reply(__(context, "error.reason"));
                    error.printStackTrace();
                }
        );
    }
}
