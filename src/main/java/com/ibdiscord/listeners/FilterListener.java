/* Copyright 2018-2020 Arraying
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
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class FilterListener extends ListenerAdapter {

    private final GuildedCache<String, Pattern> filterCache = new GuildedCache<>();

    /**
     * Receives a guild message and checks if it violates the chat filter.
     *
     * @param event The event.
     */
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        handleFilter(event.getMessage());
    }

    /**
     * When a message is updated.
     * @param event The event.
     */
    @Override
    public void onGuildMessageUpdate(GuildMessageUpdateEvent event) {
        handleFilter(event.getMessage());
    }

    /**
     * Handles filtering messages.
     * @param messageObject The JDA message object
     */
    public void handleFilter(Message messageObject) {
        Guild guild = messageObject.getGuild();
        User author = messageObject.getAuthor();
        if (author.isBot()) {
            return;
        }
        String message = messageObject.getContentRaw();
        String prefix = UDatabase.getPrefix(guild);

        if (messageObject.getContentRaw().startsWith(prefix)) {
            String messageSub = message.substring(prefix.length()).replaceAll(" +", " ");
            String[] arguments = messageSub.split(" ");
            String commandName = arguments[0].toLowerCase();
            Command command = IBai.INSTANCE.getCommandRegistry().query(commandName);
            if (command != null && command.getName().equalsIgnoreCase("filter")) {
                return;
            }
        }

        Gravity gravity = DataContainer.INSTANCE.getGravity();
        GuildData guildData = gravity.load(new GuildData(guild.getId()));
        FilterData filterData = gravity.load(new FilterData(guild.getId()));
        FilterNotifyData filterNotifyData = gravity.load(new FilterNotifyData(guild.getId()));
        if (!guildData.get(GuildData.FILTERING).defaulting(false).asBoolean()) {
            return;
        }
        Optional<Matcher> match = filterData.values().stream()
                .map(it -> filterCache.compute(guild.getIdLong(),
                        it.asString(),
                        Pattern.compile(it.asString(), Pattern.CASE_INSENSITIVE))
                )
                .map(it -> it.matcher(message))
                .filter(it -> it.find())
                .findFirst();
        if (match.isPresent()) {
            messageObject.delete().queue(success -> {
                StringBuilder builder = new StringBuilder(message);
                builder.insert(match.get().end(), "**");
                builder.insert(match.get().start(), "**");
                author.openPrivateChannel().queue(dm -> {
                    String send = String.format("The following message has been flagged and deleted for potentially "
                            + "breaking the rules on %s (offending phrase bolded):\n\n%s"
                            + "\n\n If you believe you haven't broken any rules, or have any other questions or concerns "
                            + "regarding this, you can contact the staff team for clarification by DMing the ModMail bot, "
                            + "at the top of the sidebar on the server.", guild.getName(), builder.toString());
                    send = send.length() > 2000 ? send.substring(0, 2000) : send;
                    dm.sendMessage(send).queue();
                });
                MonitorData monitorData = gravity.load(new MonitorData(guild.getId()));
                TextChannel monitorChannel = guild.getTextChannelById(
                        monitorData.get(MonitorData.MESSAGE_CHANNEL).defaulting(0).asLong()
                );
                if (monitorChannel == null) {
                    IBai.INSTANCE.getLogger().info("Monitor channel not found: %s sent %s",
                            author.getAsTag(),
                            messageObject.getContentRaw()
                    );
                    return;
                }
                String title = String.format(
                        "%s (ID: %s)",
                        author.getAsTag(),
                        author.getId()
                );

                if (title.length() > MessageEmbed.TITLE_MAX_LENGTH) {
                    title = title.substring(0, MessageEmbed.TITLE_MAX_LENGTH);
                }

                String description = String.format(
                        "\"%s\", sent in **%s**",
                        messageObject.getContentRaw(),
                        messageObject.getTextChannel().getAsMention()
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
            });
        }
    }
}

