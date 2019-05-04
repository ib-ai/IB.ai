/**
 * Copyright 2017-2019 Arraying
 *
 * This file is part of LoyalBot.
 *
 * LoyalBot is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LoyalBot is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LoyalBot. If not, see http://www.gnu.org/licenses/.
 */

package dev.vardy.command.commands;

import dev.vardy.LoyalBot;
import dev.vardy.command.Command;
import dev.vardy.command.CommandContext;
import dev.vardy.command.permissions.CommandPermission;
import dev.vardy.data.db.DContainer;
import dev.vardy.data.db.entries.GuildData;
import dev.vardy.data.db.entries.punish.PunishmentData;
import dev.vardy.data.db.entries.punish.PunishmentsData;
import dev.vardy.punish.Punishment;
import dev.vardy.punish.PunishmentHandler;
import dev.vardy.utils.UString;
import de.arraying.gravity.Gravity;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.HashSet;
import java.util.Set;

import static dev.vardy.data.db.entries.punish.PunishmentData.*;

public final class ReasonCommand extends Command {

    /**
     * Creates the command.
     */
    public ReasonCommand() {
        super("reason",
                Set.of(),
                CommandPermission.role(GuildData.MODERATOR),
                new HashSet<>()
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
        Gravity gravity = DContainer.INSTANCE.getGravity();
        PunishmentsData punishmentList = gravity.load(new PunishmentsData(guild.getId()));
        if(!punishmentList.contains(caseNumber)) {
            context.reply("That case does not exist!");
            return;
        }
        Long caseId = UString.toLong(caseNumber);
        if(caseId == null) {
            context.reply("Internal error converting to long.");
            return;
        }
        Punishment punishment = Punishment.of(context.getGuild(), caseId);
        punishment.setReason(reason);
        PunishmentData punishmentData = gravity.load(new PunishmentData(guild.getId(), caseId));
        PunishmentHandler punishmentHandler = new PunishmentHandler(guild, punishment);
        TextChannel channel = punishmentHandler.getLogChannel();
        if(channel == null) {
            context.reply("Logging is currently not enabled or set up incorrectly.");
            return;
        }
        boolean redacted = context.getOptions().stream()
                .anyMatch(it -> it.getName().equalsIgnoreCase("redacted") || it.getName().equalsIgnoreCase("redact"));
        LoyalBot.INSTANCE.getLogger().info("Redacting: " + redacted);
        punishmentData.set(REASON, reason);
        punishmentData.set(REDACTED, redacted);
        punishment.redacting(redacted);
        gravity.save(punishmentData);
        channel.editMessageById(punishmentData.get(MESSAGE).defaulting(0L).asLong(), punishment.getLogPunishment(guild, caseId)).queue(
                outstandingMove -> {
                    punishmentData.set(REASON, reason);
                    context.reply("The reason has been updated.");
                },
                error -> {
                    context.reply("An error occurred, the reason could not be updated.");
                    error.printStackTrace();
                }
        );
    }

}
