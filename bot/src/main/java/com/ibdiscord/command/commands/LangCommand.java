/**
 * Copyright 2017-2019 Jarred Vardy <jarred.vardy@gmail.com>
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

package com.ibdiscord.command.commands;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.DContainer;
import com.ibdiscord.data.db.entries.LangData;
import com.ibdiscord.localisation.Localiser;
import com.ibdiscord.utils.UDatabase;
import de.arraying.gravity.Gravity;

import java.util.Arrays;
import java.util.Set;

public final class LangCommand extends Command {

    /**
     * Creates a new command.
     */
    public LangCommand() {
        super("lang",
                Set.of("language", "l10n", "l18n"),
                CommandPermission.discord(),
                Set.of()
        );
        this.correctUsage = "lang <language>";
    }

    @Override
    protected void execute(CommandContext context) {
        if (context.getArguments().length == 0) {
            context.reply("Supported languages: " +
                    Arrays.toString(Localiser.getAllLanguages())
                        .replaceAll("(\\[)|(\\])", "") +
                        ".");
            return;
        }

        if (context.getArguments().length > 1) {
            sendUsage(context);
            return;
        }

        String language = context.getArguments()[0];

        if (!Arrays.asList(Localiser.getAllLanguages()).contains(language)) {
            context.reply(String.format("'%s' is not a supported language. Use %slang to see a list of supported languages.", language, UDatabase.getPrefix(context.getGuild())));
        } else {
            Gravity gravity = DContainer.INSTANCE.getGravity();
            LangData langData = gravity.load(new LangData());
            langData.set(context.getMember().getUser().getId(), language);
            gravity.save(langData);

            context.reply("Successfully updated your language preferences.");
        }
    }
}
