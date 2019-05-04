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

package dev.vardy.listeners;

import dev.vardy.LoyalBot;
import dev.vardy.data.db.DContainer;
import dev.vardy.data.db.entries.monitor.MonitorData;
import dev.vardy.data.db.entries.monitor.MonitorMessageData;
import dev.vardy.data.db.entries.monitor.MonitorUserData;
import dev.vardy.utils.objects.GuildedCache;

import de.arraying.gravity.Gravity;
import de.arraying.gravity.data.property.Property;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class MonitorListener extends ListenerAdapter {

    private GuildedCache<String, Pattern> regexCache = new GuildedCache<>();

    /**
     * When a message is received.
     * This is separate from the command handler, so nothing returns prematurely.
     * @param event The event.
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getAuthor().isBot()) {
            return;
        }
        Message message = event.getMessage();
        Gravity gravity = DContainer.INSTANCE.getGravity();
        MonitorData monitorData = gravity.load(new MonitorData(message.getGuild().getId()));
        if(!monitorData.get(MonitorData.ENABLED).defaulting(false).asBoolean()) {
            return;
        }
        MonitorUserData monitorUserData = gravity.load(new MonitorUserData(message.getGuild().getId()));
        MonitorMessageData monitorMessageData = gravity.load(new MonitorMessageData(message.getGuild().getId()));
        List<Long> users = monitorUserData.values().stream()
                .map(Property::asLong)
                .collect(Collectors.toList());
        if(users.stream().anyMatch(it -> message.getAuthor().getIdLong() == it)) {
            logSuspicion(monitorData, message, MonitorData.USER_CHANNEL);
            return;
        }
        for(Property property : monitorMessageData.values()) {
            String string = property.asString();
            if(string == null) {
                continue;
            }
            Pattern pattern = regexCache.compute(message.getGuild().getIdLong(), string, Pattern.compile(string));
            if(pattern.matcher(message.getContentRaw()).find()) {
                logSuspicion(monitorData, message, MonitorData.MESSAGE_CHANNEL);
                break;
            }
        }
    }

    /**
     * Logs a suspicious message.
     * @param monitorData The monitor data, so it doesn't need to be loaded again.
     * @param channel The key to the channel to use.
     * @param message The message.
     */
    private void logSuspicion(MonitorData monitorData, Message message, String channel) {
        TextChannel textChannel = message.getGuild().getTextChannelById(monitorData.get(channel).defaulting(0).asLong());
        if(textChannel == null) {
            LoyalBot.INSTANCE.getLogger().info("Monitor channel not found: %s sent %s", message.getAuthor().getAsTag(), message.getContentRaw());
            return;
        }
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setColor(Color.RED)
                .setAuthor("Monitor Trigger")
                .setTitle(message.getAuthor().getAsTag())
                .setDescription(message.getContentRaw())
                .addField("Utilities", "[21 Jump Street](" + message.getJumpUrl() + ")", false);
        textChannel.sendMessage(embedBuilder.build()).queue();
    }

}
