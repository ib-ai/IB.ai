package com.ibdiscord.listeners;

import com.ibdiscord.IBai;
import com.ibdiscord.data.db.DContainer;
import com.ibdiscord.data.db.entries.monitor.MonitorData;
import com.ibdiscord.data.db.entries.monitor.MonitorMessageData;
import com.ibdiscord.data.db.entries.monitor.MonitorUserData;
import com.ibdiscord.utils.objects.GuildedCache;
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

/**
 * Copyright 2019 Arraying
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
            logSuspicion(monitorData, message);
            return;
        }
        for(Property property : monitorMessageData.values()) {
            String string = property.asString();
            if(string == null) {
                continue;
            }
            Pattern pattern = regexCache.compute(message.getGuild().getIdLong(), string, Pattern.compile(string));
            if(pattern.matcher(message.getContentRaw()).find()) {
                logSuspicion(monitorData, message);
                break;
            }
        }
    }

    /**
     * Logs a suspicious message.
     * @param monitorData The monitor data, so it doesn't need to be loaded again.
     * @param message The message.
     */
    private void logSuspicion(MonitorData monitorData, Message message) {
        TextChannel textChannel = message.getGuild().getTextChannelById(monitorData.get(MonitorData.CHANNEL).defaulting(0).asLong());
        if(textChannel == null) {
            IBai.INSTANCE.getLogger().info("Monitor channel not found: %s sent %s", message.getAuthor().getAsTag(), message.getContentRaw());
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
