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
import com.ibdiscord.data.db.entries.tag.TagActiveData;

import java.util.List;

public final class TagActivate implements CommandAction {

    /**
     * Toggles the activated status of a command.
     * @param context The command context.
     */
    @Override
    public void accept(CommandContext context) {
        context.assertArguments(1, "error.generic_syntax_arg");
        List<String> data = context.assertQuotes(null, "error.tag_input");
        String trigger = context.assertRegex(data.get(0), "error.tag_expression");

        TagActiveData tagActiveData = DataContainer.INSTANCE.getGravity()
                .load(new TagActiveData(context.getGuild().getId()));
        if(tagActiveData.contains(trigger)) {
            tagActiveData.remove(trigger);
            context.replyI18n("info.tag_enabled");
        } else {
            tagActiveData.add(trigger);
            context.replyI18n("info.tag_disabled");
        }
        DataContainer.INSTANCE.getGravity().save(tagActiveData);
    }

}
