package com.ibdiscord.command.commands.tag;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.TagData;
import com.ibdiscord.localisation.Localiser;
import com.ibdiscord.pagination.Pagination;
import com.ibdiscord.utils.UInput;
import de.arraying.gravity.Gravity;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Copyright 2017-2019 Ray Clark
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
public final class TagFindCommand extends Command {

    /**
     * Creates the command.
     */
    public TagFindCommand() {
        super("find",
                Set.of("lookup"),
                CommandPermission.discord(),
                Set.of()
        );
        this.correctUsage = "tag find \"tag name\" [page number]";
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
        String compare = UInput.extractQuotedStrings(context.getArguments()).get(0);
        Gravity gravity = DataContainer.INSTANCE.getGravity();
        List<String> matches = new ArrayList<>();
        Set<String> tagKeys = gravity.load(new TagData(guild)).getKeys();
        Pattern pattern = Pattern.compile(compare);
        for(String key : tagKeys) {
            if(pattern.matcher(key).matches())
                matches.add(key);
        }
        EmbedBuilder embedBuilder = new EmbedBuilder();
        StringBuilder tags = new StringBuilder();
        Pagination<String> pagination = new Pagination<>(matches, 20);
        int page = 1;
        if(context.getArguments().length >= 2) {
            try {
                page = Integer.valueOf(context.getArguments()[1]);
            } catch (NumberFormatException ignored) {
            }
        }
        pagination.page(page).forEach(entry -> {
            tags.append(entry.getValue()).append(", ");
        });
        if (tags.length() == 0) {
            embedBuilder.addField(Localiser.__(context, "error.tag_404"), "", false);
        } else {
            embedBuilder.addField(Localiser.__(context, "info.tag_list_similar"), tags.substring(0, tags.length() - 2), false);
            embedBuilder.setFooter(Localiser.__(context, "info.paginated",String.valueOf(page),
                    String.valueOf(pagination.total())), null);
        }
        context.reply(embedBuilder.build());
    }

}
