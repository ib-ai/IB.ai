/* Copyright 2017-2020 Arraying
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

package com.ibdiscord.command.actions;

import com.ibdiscord.command.CommandAction;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.FilterData;
import com.ibdiscord.utils.UString;
import de.arraying.gravity.Gravity;

public final class FilterCreate implements CommandAction {

    /**
     * Creates a filtered value.
     * @param context The command context.
     */
    @Override
    public void accept(CommandContext context) {
        context.assertArguments(1, "error.generic_syntax_arg");
        String input = UString.concat(context.getArguments(), " ", 0);
        context.assertRegex(input, "error.filter");

        Gravity gravity = DataContainer.INSTANCE.getGravity();
        FilterData filterData = gravity.load(new FilterData(context.getGuild().getId()));
        filterData.add(input);
        gravity.save(filterData);
        context.replyI18n("success.filter_add");
    }

}
