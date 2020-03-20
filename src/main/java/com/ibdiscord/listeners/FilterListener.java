/* Copyright 2017-2019 Arraying
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

package com.ibdiscord.listeners;

import com.ibdiscord.IBai;
import com.ibdiscord.command.Command;
import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.GuildData;
import com.ibdiscord.data.db.entries.filter.FilterData;
import com.ibdiscord.data.db.entries.filter.FilterNotifyData;
import com.ibdiscord.data.db.entries.monitor.MonitorData;
import com.ibdiscord.utils.UDatabase;
import com.ibdiscord.utils.objects.GuildedCache;
import de.arraying.gravity.Gravity;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.Color;
import java.util.Optional;
import java.util.regex.Pattern;

public final class FilterListener extends ListenerAdapter {

    private final GuildedCache<String, Pattern> filterCache = new GuildedCache<>();

    /**
     * Receives a guild message and checks if it violates the chat filter.
     * @param event The event.
     */
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if(event.getAuthor().isBot()) {
            return;
        }
        String message = event.getMessage().getContentRaw();
        String prefix = UDatabase.getPrefix(event.getGuild());

        if(event.getMessage().getContentRaw().startsWith(prefix)) {
            String messageSub = message.substring(prefix.length()).replaceAll(" +", " ");
            String[] arguments = messageSub.split(" ");
            String commandName = arguments[0].toLowerCase();
            Command command = Command.find(null, commandName);
            if(command != null && command.getName().equalsIgnoreCase("filter")) {
                return;
            }
        }

        Gravity gravity = DataContainer.INSTANCE.getGravity();
        GuildData guildData = gravity.load(new GuildData(event.getGuild().getId()));
        FilterData filterData = gravity.load(new FilterData(event.getGuild().getId()));
        FilterNotifyData filterNotifyData = gravity.load(new FilterNotifyData(event.getGuild().getId()));
        if(!guildData.get(GuildData.FILTERING).defaulting(false).asBoolean()) {
            return;
        }
        Optional<Pattern> match = filterData.values().stream()
                .map(it -> filterCache.compute(event.getGuild().getIdLong(),
                        it.asString(),
                        Pattern.compile(it.asString()))
                ).filter(it -> it.matcher(message).matches())
                .findFirst();
        if(match.isPresent()) {
            event.getMessage().delete().queue(success -> {
                    event.getAuthor().openPrivateChannel().queue(dm -> {
                        String send = String.format("The following message has been automatically flagged and "
                                + "removed from %s:\n\n%s", event.getGuild().getName(), message);
                        send = send.length() > 2000 ? send.substring(0, 2000) : send;
                        dm.sendMessage(send).queue();
                    });
                    MonitorData monitorData = gravity.load(new MonitorData(event.getGuild().getId()));
                    TextChannel monitorChannel = event.getGuild().getTextChannelById(
                            monitorData.get(MonitorData.MESSAGE_CHANNEL).defaulting(0).asLong()
                    );
                    if(monitorChannel == null) {
                        IBai.INSTANCE.getLogger().info("Monitor channel not found: %s sent %s",
                                event.getAuthor().getAsTag(),
                                event.getMessage().getContentRaw()
                        );
                        return;
                    }

                    String title = String.format(
                            "%s (ID: %s)",
                            event.getMessage().getAuthor().getAsTag(),
                            event.getMessage().getAuthor().getId()
                    );

                    if (title.length() > MessageEmbed.TITLE_MAX_LENGTH) {
                        title = title.substring(0, MessageEmbed.TITLE_MAX_LENGTH);
                    }

                    String description = String.format(
                            "\"%s\", sent in **%s**",
                            event.getMessage().getContentRaw(),
                            event.getChannel().getName()
                    );

                    description = description.length() > 2000 ? description.substring(0, 2000) : description;
                    EmbedBuilder embedBuilder = new EmbedBuilder()
                            .setColor(Color.MAGENTA)
                            .setAuthor("Filter was triggered!")
                            .setTitle(title)
                            .setDescription(description);
                    monitorChannel.sendMessage(embedBuilder.build()).queue();
                    if (!filterNotifyData.contains(match.get().pattern())) {
                        monitorChannel.sendMessage("@here").queue();
                    }
                }
            );
        }
    }

}
