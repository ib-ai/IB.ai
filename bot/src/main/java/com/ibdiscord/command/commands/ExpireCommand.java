package com.ibdiscord.command.commands;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.GuildData;
import com.ibdiscord.data.db.entries.punish.ExpiryData;
import com.ibdiscord.data.db.entries.punish.PunishmentsData;
import com.ibdiscord.localisation.Localiser;
import com.ibdiscord.punish.Punishment;
import com.ibdiscord.punish.PunishmentExpiry;
import com.ibdiscord.utils.UTime;
import de.arraying.gravity.Gravity;

import java.util.Set;
import java.util.concurrent.ScheduledFuture;

/**
 * Copyright 2017-2019 Arraying
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
public final class ExpireCommand extends Command {

    /**
     * Creates the command.
     */
    public ExpireCommand() {
        super("expire",
                Set.of("expiry"),
                CommandPermission.role(GuildData.MODERATOR),
                Set.of()
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
        Gravity gravity = DataContainer.INSTANCE.getGravity();
        String guildId = context.getGuild().getId();
        String caseNumber = context.getArguments()[0];
        long expires = UTime.parseDuration(context.getArguments()[1]);
        if(expires == -1) {
            context.reply(Localiser.__(context, "error.expire_duration"));
            return;
        }
        long delay = expires - System.currentTimeMillis();
        if(delay <= 0) {
            context.reply(Localiser.__(context, "error.expire_expired"));
            return;
        }
        PunishmentsData list = gravity.load(new PunishmentsData(guildId));
        if(!list.contains(caseNumber)) {
            context.reply(Localiser.__(context, "error.expire_case"));
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
        context.reply(Localiser.__(context, "success.expire"));
    }

}
