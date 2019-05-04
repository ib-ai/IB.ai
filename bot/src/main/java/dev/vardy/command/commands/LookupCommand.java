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
import dev.vardy.data.db.entries.punish.PunishmentsData;
import dev.vardy.punish.Punishment;
import dev.vardy.utils.UString;
import de.arraying.gravity.Gravity;
import net.dv8tion.jda.core.entities.Guild;

import java.util.HashSet;
import java.util.Set;

public final class LookupCommand extends Command {

    /**
     * Creates the command.
     */
    public LookupCommand() {
        super("lookup",
                Set.of(),
                CommandPermission.role(GuildData.MODERATOR),
                new HashSet<>()
        );
        this.correctUsage = "lookup <case ID>";
    }

    /**
     * Looks up a punishment and gives unlimited info.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        if(context.getArguments().length < 1) {
            sendUsage(context);
            return;
        }
        Guild guild = context.getGuild();
        String caseNumber = context.getArguments()[0];
        Gravity gravity = DContainer.INSTANCE.getGravity();
        PunishmentsData punishmentList = gravity.load(new PunishmentsData(guild.getId()));
        if(!punishmentList.contains(caseNumber)) {
            context.reply("That case does not exist!");
            return;
        }
        Long caseId = UString.toLong(caseNumber);
        if(caseId == null) {
            context.reply("Internal error converting long.");
            return;
        }
        Punishment punishment = Punishment.of(context.getGuild(), caseId);
        context.reply(punishment.redacting(false)
                .getLogPunishment(context.getGuild(), caseId));
    }

}
