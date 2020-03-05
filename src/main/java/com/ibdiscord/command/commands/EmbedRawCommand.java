/* Copyright 2017-2020 Ray Clark
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

package com.ibdiscord.command.commands;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.Option;
import com.ibdiscord.command.permissions.CommandPermission;
import de.arraying.kotys.JSON;
import de.arraying.kotys.JSONArray;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.Set;

public final class EmbedRawCommand extends Command {

    /**
     * Creates the command.
     */
    public EmbedRawCommand() {
        super("embedraw",
                CommandPermission.discord(),
                Set.of()
        );
        this.correctUsage = "embedraw <channel> [-update message id] <json>";
    }

    /**
     * Shows a list of sub commands.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        if (context.getArguments().length < 2) {
            sendUsage(context);
            return;
        }

        if (context.getMessage().getMentionedChannels().size() < 1) {
            context.reply(__(context, "error.missing_channel"));
            return;
        }

        StringBuilder jsonBuilder = new StringBuilder();
        for (int i = 1; i < context.getArguments().length; i++) {
            jsonBuilder.append(context.getArguments()[i]);
        }

        JSON embed;

        try {
            embed = new JSON(jsonBuilder.toString());
        } catch (IllegalStateException e) {
            context.reply(__(context, "error.missing_data"));
            return;
        }

        EmbedBuilder builder = new EmbedBuilder();

        if (embed.has("description")) {
            String description = embed.string("description");
            if(description.length() > MessageEmbed.TEXT_MAX_LENGTH) {
                description = description.substring(0, MessageEmbed.TEXT_MAX_LENGTH);
            }
            builder.setDescription(description);
        }

        if (embed.has("colour")) {
            try {
                Color color = Color.decode(embed.string("colour"));
                builder.setColor(color);
            } catch(Exception exception) {
                context.getChannel().sendMessage(__(context, "error.invalid_colour")).queue();
                return;
            }
        }

        if (embed.has("image_url")) {
            try {
                String image = embed.string("image_url");
                new URL(image);
                builder.setImage(image);
            } catch(MalformedURLException exception) {
                context.getChannel().sendMessage(__(context, "error.invalid_url")).queue();
                return;
            }
        }

        if (embed.has("fields")) {
            JSONArray fields = embed.array("fields");

            if (fields.length() > 20) {
                context.getChannel().sendMessage(__(context, "error.exception")).queue();
                return;
            }

            for (int i = 0; i < fields.length(); i++) {
                JSONArray tuple = fields.array(i);

                if (tuple.length() < 2) {
                    continue;
                }
                
                String field = tuple.string(0);
                String value = tuple.string(1);
                if(field.length() > MessageEmbed.TITLE_MAX_LENGTH) {
                    field = field.substring(0, MessageEmbed.TITLE_MAX_LENGTH);
                }
                if(value.length() > MessageEmbed.VALUE_MAX_LENGTH) {
                    value = value.substring(0, MessageEmbed.VALUE_MAX_LENGTH);
                }
                builder.addField(field, value, false);
            }
        }

        if (builder.isEmpty()) {
            context.reply(__(context, "error.missing_data"));
            return;
        }

        TextChannel target = context.getMessage().getMentionedChannels().get(0);
        if(!Objects.requireNonNull(context.getMessage().getMember())
                .hasPermission(target, Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS)) {
            context.getChannel().sendMessage(__(context, "error.missing_channel_permissions")).queue();
            return;
        }

        if (!context.getOptions().stream()
                .anyMatch(it -> it.getName().equalsIgnoreCase("update"))) {
            target.sendMessage(builder.build()).queue();
        } else {
            try {
                for (Option option : context.getOptions()) {
                    if (option.getName().equalsIgnoreCase("update")) {
                        long id = Long.valueOf(option.getValue());
                        target.retrieveMessageById(id)
                                .queue(success -> target.editMessageById(id, builder.build()).queue(),
                                    failure -> target.sendMessage(__(context, "error.pin_channel"))
                                            .queue());
                        break;
                    }
                }
            } catch(NumberFormatException exception) {
                return;
            }
        }
    }

}
