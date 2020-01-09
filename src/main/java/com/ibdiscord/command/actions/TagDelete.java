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
import com.ibdiscord.data.db.entries.tag.TagData;

import java.util.List;

public final class TagDelete implements CommandAction {

    /**
     * Deletes a tag.
     * @param context The command context.
     */
    @Override
    public void accept(CommandContext context) {
        context.assertArguments(1, "error.generic_syntax_arg");
        List<String> names = context.assertQuotes(1, "error.tag_quotation");
        TagData tagData = DataContainer.INSTANCE.getGravity().load(new TagData(context.getGuild().getId()));
        names.forEach(tag -> {
            tagData.unset(names.get(0));
            context.replyI18n("success.tag_remove");
        });
        DataContainer.INSTANCE.getGravity().save(tagData);
    }

}
