package com.ibdiscord.command.commands;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.GuildData;
import com.ibdiscord.data.db.entries.NoteData;
import com.ibdiscord.localisation.Localiser;
import com.ibdiscord.utils.UInput;
import com.ibdiscord.utils.UString;
import de.arraying.gravity.Gravity;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageEmbed;

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
public final class NoteCommand extends Command {

    /**
     * Creates the command.
     */
    public NoteCommand() {
        super("note",
                Set.of("notes"),
                CommandPermission.role(GuildData.MODERATOR),
                Set.of()
        );
        this.correctUsage = "note <user> [new note]";
    }

    @Override
    protected void execute(CommandContext context) {
        if(context.getArguments().length == 0) {
            context.reply(Localiser.__(context, "error.note_empty"));
            return;
        }
        Member member = UInput.getMember(context.getGuild(), context.getArguments()[0]);
        if(member == null) {
            context.reply(Localiser.__(context, "error.note_invalid"));
            return;
        }
        Gravity gravity = DataContainer.INSTANCE.getGravity();
        NoteData noteData = gravity.load(new NoteData(context.getGuild().getId(), member.getUser().getId()));
        if(context.getArguments().length == 1) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setDescription(Localiser.__(context, "info.note"));
            noteData.values().stream()
                    .map(it -> it.defaulting("N/A:N/A").toString())
                    .forEach(it -> {
                        String user;
                        String data;
                        int index = it.indexOf(":");
                        if(index < 0) {
                            user = "???";
                            data = it;
                        } else {
                            user = it.substring(0, index);
                            data = it.substring(index);
                            if(data.length() > 1) {
                                data = data.substring(1);
                            }
                        }
                        embedBuilder.addField(Localiser.__(context, "info.note_author", user),
                                data,
                                false);
                    });
            context.reply(embedBuilder.build());
        } else {
            String note = UString.concat(context.getArguments(), " ", 1);
            if(note.length() > MessageEmbed.VALUE_MAX_LENGTH) {
                context.reply(Localiser.__(context, "error.note_long", String.valueOf(MessageEmbed.VALUE_MAX_LENGTH)));
                return;
            }
            if(noteData.size() >= 25) {
                context.reply(Localiser.__(context, "error.note_full"));
                return;
            }
            String data = context.getMember().getUser().getId() + ":" + note;
            noteData.add(data);
            gravity.save(noteData);
            context.reply(Localiser.__(context, "success.note"));
        }
    }

}
