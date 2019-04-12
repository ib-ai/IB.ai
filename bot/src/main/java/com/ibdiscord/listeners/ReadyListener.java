package com.ibdiscord.listeners;

import com.ibdiscord.IBai;
import com.ibdiscord.api.APICaller;
import com.ibdiscord.api.Route;
import com.ibdiscord.api.result.BodyResultHandler;
import com.ibdiscord.data.db.DContainer;
import com.ibdiscord.data.db.entries.punish.ExpiryData;
import com.ibdiscord.data.db.entries.reminder.ReminderData;
import com.ibdiscord.data.db.entries.reminder.ReminderUserData;
import com.ibdiscord.punish.Punishment;
import com.ibdiscord.punish.PunishmentExpiry;
import com.ibdiscord.reminder.Reminder;
import com.ibdiscord.reminder.ReminderHandler;
import de.arraying.gravity.Gravity;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.slf4j.Logger;

/**
 * Copyright 2019 Jarred Vardy, Arraying
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
public final class ReadyListener extends ListenerAdapter {

    /**
     * When the bot is marked as ready.
     * @param event The event.
     */
    @Override
    public void onReady(ReadyEvent event) {
        event.getJDA().asBot().getApplicationInfo().queue(appInfo -> {
            String botName = appInfo.getName();
            String botOwner = appInfo.getOwner().getName();
            String botDescription = appInfo.getDescription();
            boolean isPublicBot = appInfo.isBotPublic();
            int guildNum = event.getJDA().getGuilds().size();
            Logger logger = IBai.INSTANCE.getLogger();
            logger.info("Bot \"{}\" by \"{}\" is now connected.", botName, botOwner);
            logger.info("Currently serving {} guilds.", guildNum);
            logger.info("Described as \"{}\", {}.", botDescription, (isPublicBot ? "public" : "private"));
            Gravity gravity = DContainer.INSTANCE.getGravity();
            for(Guild guild : event.getJDA().getGuilds()) {
                ExpiryData expiryData = gravity.load(new ExpiryData(guild.getId()));
                for(String key : expiryData.getKeys()) {
                    long expiry = expiryData.get(key).asLong();
                    Punishment punishment = Punishment.of(guild, key);
                    if(System.currentTimeMillis() > expiry) {
                        PunishmentExpiry.INSTANCE.expire(guild, key, punishment);
                    } else {
                        PunishmentExpiry.INSTANCE.schedule(guild, key, expiry - System.currentTimeMillis(), punishment);
                    }
                }
            }
            ReminderData reminderData = gravity.load(new ReminderData());
            for(int i = 1; i <= reminderData.get().defaulting(0).asInt(); i++) {
                ReminderUserData reminderUserData = gravity.load(new ReminderUserData(i));
                String userId = reminderUserData.get(ReminderUserData.USER).asString();
                String text = reminderUserData.get(ReminderUserData.TEXT).asString();
                long time = reminderUserData.get(ReminderUserData.TIME).asLong();
                User user = event.getJDA().getUserById(userId);
                ReminderHandler.INSTANCE.schedule(new Reminder(i, time, text), user);
            }
        }, error -> {
            error.printStackTrace();
            System.exit(1);
        });
        long now = System.currentTimeMillis();
        boolean success = APICaller.INSTANCE.dispatch(Route.PING, new BodyResultHandler((status, json) -> {
            long server = json.large("received");
            IBai.INSTANCE.getLogger().info("API latency is at {} ms.", server - now);
        }));
        if(!success) {
            IBai.INSTANCE.getLogger().info("API offline.");
        }
    }

}
