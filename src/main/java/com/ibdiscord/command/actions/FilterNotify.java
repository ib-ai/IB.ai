/* Copyright 2020 Ray Clark
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
import com.ibdiscord.data.db.entries.filter.FilterNotifyData;
import com.ibdiscord.utils.UString;

public final class FilterNotify implements CommandAction {

    /**
     * Toggles the activated status of a command.
     * @param context The command context.
     */
    @Override
    public void accept(CommandContext context) {
        context.assertArguments(1, "error.generic_syntax_arg");
        String trigger = context.assertRegex(UString.concat(context.getArguments(), " ", 0), "error.filter");

        FilterNotifyData filterNotifyData = DataContainer.INSTANCE.getGravity()
                .load(new FilterNotifyData(context.getGuild().getId()));
        if(filterNotifyData.contains(trigger)) {
            filterNotifyData.remove(trigger);
            context.replyI18n("info.filter_enabled");
        } else {
            filterNotifyData.add(trigger);
            context.replyI18n("info.filter_disabled");
        }
        DataContainer.INSTANCE.getGravity().save(filterNotifyData);
    }

}
