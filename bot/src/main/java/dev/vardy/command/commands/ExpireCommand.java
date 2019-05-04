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

import dev.vardy.command.Command;
import dev.vardy.command.CommandContext;
import dev.vardy.command.permissions.CommandPermission;
import dev.vardy.data.db.DContainer;
import dev.vardy.data.db.entries.GuildData;
import dev.vardy.data.db.entries.punish.ExpiryData;
import dev.vardy.data.db.entries.punish.PunishmentsData;
import dev.vardy.punish.Punishment;
import dev.vardy.punish.PunishmentExpiry;
import dev.vardy.utils.UTime;
import de.arraying.gravity.Gravity;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;

public final class ExpireCommand extends Command {

    /**
     * Creates the command.
     */
    public ExpireCommand() {
        super("expire",
                Set.of("expiry"),
                CommandPermission.role(GuildData.MODERATOR),
                new HashSet<>()
        );
        this.correctUsage = "expire <case> <time>";
    }

    /**
     * Sets a punishment to expire.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        if(context.getArguments().length < 2) {
            sendUsage(context);
            return;
        }
        Gravity gravity = DContainer.INSTANCE.getGravity();
        String guildId = context.getGuild().getId();
        String caseNumber = context.getArguments()[0];
        long expires = UTime.parseDuration(context.getArguments()[1]);
        if(expires == -1) {
            context.reply("The duration provided is invalid.");
            return;
        }
        long delay = expires - System.currentTimeMillis();
        if(delay <= 0) {
            context.reply("That time has already passed.");
            return;
        }
        PunishmentsData list = gravity.load(new PunishmentsData(guildId));
        if(!list.contains(caseNumber)) {
            context.reply("The case provided is invalid.");
            return;
        }
        ExpiryData expiryData = gravity.load(new ExpiryData(guildId));
        Punishment punishment = Punishment.of(context.getGuild(), caseNumber);
        ScheduledFuture<?> scheduled = PunishmentExpiry.INSTANCE.getFor(caseNumber);
        if(scheduled != null) {
            scheduled.cancel(true);
        }
        PunishmentExpiry.INSTANCE.schedule(context.getGuild(), caseNumber, delay, punishment);
        expiryData.set(caseNumber, expires);
        gravity.save(expiryData);
        context.reply("The expiration has been scheduled.");
    }

}
