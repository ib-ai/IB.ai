package com.ibdiscord.command.commands.filter;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.FilterData;
import com.ibdiscord.utils.UInput;
import com.ibdiscord.utils.UString;
import de.arraying.gravity.Gravity;
import net.dv8tion.jda.api.Permission;

import java.util.Set;

/**
 * Copyright 2017-2019 Arraying
 * <p>
 * This file is part of IB.ai.
 * <p>
 * IB.ai is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * IB.ai is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with IB.ai. If not, see http://www.gnu.org/licenses/.
 */
public final class FilterCreateCommand extends Command {

    /**
     * Creates the command.
     */
    FilterCreateCommand() {
        super("create",
                Set.of("c", "add", "a"),
                CommandPermission.discord(Permission.MANAGE_SERVER),
                Set.of()
        );
    }

    /**
     * Adds a regular expression to the filter.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        if(context.getArguments().length < 1) {
            sendUsage(context);
            return;
        }
        String input = UString.concat(context.getArguments(), " ", 0);
        if(!UInput.isValidRegex(input)) {
            context.reply("The filter you provided is an invalid regular expression. Did you forget to escape any special characters?");
            return;
        }
        Gravity gravity = DataContainer.INSTANCE.getGravity();
        FilterData filterData = gravity.load(new FilterData(context.getGuild().getId()));
        filterData.add(input);
        gravity.save(filterData);
        context.reply("The filter has been added.");
    }

}
