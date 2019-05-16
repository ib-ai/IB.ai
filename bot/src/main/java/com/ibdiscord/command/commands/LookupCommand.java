package com.ibdiscord.command.commands;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.DContainer;
import com.ibdiscord.data.db.entries.GuildData;
import com.ibdiscord.data.db.entries.punish.PunishmentsData;
import com.ibdiscord.punish.Punishment;
import com.ibdiscord.utils.UString;
import de.arraying.gravity.Gravity;
import net.dv8tion.jda.core.entities.Guild;

import java.util.Set;

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
public final class LookupCommand extends Command {

    /**
     * Creates the command.
     */
    public LookupCommand() {
        super("lookup",
                Set.of(),
                CommandPermission.role(GuildData.MODERATOR),
                Set.of()
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
