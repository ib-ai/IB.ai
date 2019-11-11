/* Copyright 2017-2019 Jarred Vardy <jarred.vardy@gmail.com>
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

package com.ibdiscord.command.commands.lang;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.LangData;
import com.ibdiscord.localisation.Localiser;
import com.ibdiscord.utils.UDatabase;
import de.arraying.gravity.Gravity;

import java.util.Set;

public final class LangSetCommand extends Command {

    /**
     * Creates a new LangSet command.
     */
    LangSetCommand() {
        super("lang_set",
                CommandPermission.discord(),
                Set.of()
        );

        this.correctUsage = "lang set [language]";
    }

    @Override
    protected void execute(CommandContext context) {
        if(context.getArguments().length > 1) {
            sendUsage(context);
            return;
        }

        String language = context.getArguments()[0].toLowerCase();

        if(!Localiser.getAllLanguageCodes().contains(language) && !Localiser.getAllLanguages().contains(language)) {
            context.reply(__(context, "error.lang_command", language, UDatabase.getPrefix(context.getGuild())));
        } else {
            Gravity gravity = DataContainer.INSTANCE.getGravity();
            LangData langData = gravity.load(new LangData());
            String finalLang = Localiser.getAllLanguageCodes().contains(language)
                    ? language
                    : Localiser.getLanguageCode(language);
            // if(finalLang == null) {
            //     context.reply(__(context, "error.lang_command", language, UDatabase.getPrefix(context.getGuild())));
            //     return;
            // }
            langData.set(context.getMember().getUser().getId(), finalLang);
            gravity.save(langData);

            context.reply(__(context, "success.lang_command"));
        }
    }
}
