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
import com.ibdiscord.data.db.entries.GuildData;
import com.ibdiscord.data.db.entries.punish.PunishmentsData;
import com.ibdiscord.pagination.Pagination;
import com.ibdiscord.punish.Punishment;
import com.ibdiscord.utils.objects.Tuple;
import de.arraying.gravity.Gravity;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class FindCommand extends Command {

    /**
     * Creates the command.
     */
    public FindCommand() {
        super("find",
                CommandPermission.role(GuildData.MODERATOR),
                Set.of()
        );
        this.correctUsage = "find <user ID> [page number]";
    }

    /**
     * Attempts to find all punishments for the user ID.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        if(context.getArguments().length < 1) {
            sendUsage(context);
            return;
        }
        String guild = context.getGuild().getId();
        String compare = context.getArguments()[0];
        Gravity gravity = DataContainer.INSTANCE.getGravity();
        List<Tuple<Punishment, Long>> punishments = new ArrayList<>();
        long max = gravity.load(new PunishmentsData(guild)).size();
        for(long i = 1; i <= max; i++) {
            Punishment punishment = Punishment.of(context.getGuild(), i);
            if((punishment.getUserId() != null
                    && punishment.getUserId().equalsIgnoreCase(compare))
                    || (punishment.getStaffId() != null
                    && punishment.getStaffId().equalsIgnoreCase(compare))) {
                punishments.add(new Tuple<>(punishment, i));
            }
        }
        EmbedBuilder embedBuilder = new EmbedBuilder();
        Pagination<Tuple<Punishment, Long>> pagination = new Pagination<>(punishments, 20);
        int page = 1;
        if(context.getArguments().length >= 2) {
            try {
                page = Integer.valueOf(context.getArguments()[1]);
            } catch(NumberFormatException ex) {
                // Ignored
            }
        }
        pagination.page(page).forEach(entry -> {
            Punishment punishment = entry.getValue().getPropertyA();
            embedBuilder.addField(String.format("Case #%d:", entry.getValue().getPropertyB()),
                    String.format("%s punished %s", punishment.getStaffDisplay(), punishment.getUserDisplay()),
                    false);
        });
        embedBuilder.setFooter("Page " + page + "/" + pagination.total(), null);
        context.reply(embedBuilder.build());
    }

}
