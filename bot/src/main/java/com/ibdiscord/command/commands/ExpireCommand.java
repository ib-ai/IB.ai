package com.ibdiscord.command.commands;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.DContainer;
import com.ibdiscord.data.db.entries.GuildData;
import com.ibdiscord.data.db.entries.punish.ExpiryData;
import com.ibdiscord.data.db.entries.punish.PunishmentsData;
import com.ibdiscord.punish.Punishment;
import com.ibdiscord.punish.PunishmentExpiry;
import com.ibdiscord.utils.UTime;
import de.arraying.gravity.Gravity;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;

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
