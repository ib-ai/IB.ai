/**
 * Copyright 2017-2019 Arraying
 *
 * This file is part of LoyalBot.
 *
 * LoyalBot is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LoyalBot is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LoyalBot. If not, see http://www.gnu.org/licenses/.
 */

package dev.vardy.command.commands;

import dev.vardy.command.Command;
import dev.vardy.command.CommandContext;
import dev.vardy.command.permissions.CommandPermission;
import dev.vardy.data.db.DContainer;
import dev.vardy.data.db.entries.GuildData;
import dev.vardy.data.db.entries.NoteData;
import dev.vardy.utils.UInput;
import dev.vardy.utils.UString;
import de.arraying.gravity.Gravity;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.util.HashSet;
import java.util.Set;

public final class NoteCommand extends Command {

    /**
     * Creates the command.
     */
    public NoteCommand() {
        super("note",
                Set.of("notes"),
                CommandPermission.role(GuildData.MODERATOR),
                new HashSet<>()
        );
        this.correctUsage = "note <user> [new note]";
    }

    @Override
    protected void execute(CommandContext context) {
        if(context.getArguments().length == 0) {
            context.reply("Please provide a user to add a note to.");
            return;
        }
        Member member = UInput.getMember(context.getGuild(), context.getArguments()[0]);
        if(member == null) {
            context.reply("Invalid member.");
            return;
        }
        Gravity gravity = DContainer.INSTANCE.getGravity();
        NoteData noteData = gravity.load(new NoteData(context.getGuild().getId(), member.getUser().getId()));
        if(context.getArguments().length == 1) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setDescription("Here are the notes for the user.");
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
                        embedBuilder.addField("Entry by " + user + ":",
                                data,
                                false);
                    });
            context.reply(embedBuilder.build());
        } else {
            String note = UString.concat(context.getArguments(), " ", 1);
            if(note.length() > MessageEmbed.VALUE_MAX_LENGTH) {
                context.reply("Your note is too long! Please make it less than " + MessageEmbed.VALUE_MAX_LENGTH + " characters.");
                return;
            }
            if(noteData.size() >= 25) {
                context.reply("There are already too many notes on this user (Discord limitation).");
                return;
            }
            String data = context.getMember().getUser().getId() + ":" + note;
            noteData.add(data);
            gravity.save(noteData);
            context.reply("The note has been added.");
        }
    }

}
