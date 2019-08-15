package com.ibdiscord.listeners;

import com.ibdiscord.IBai;
import com.ibdiscord.api.APICaller;
import com.ibdiscord.api.Route;
import com.ibdiscord.api.result.BodyResultHandler;
import com.ibdiscord.data.db.DataContainer;
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
 * Copyright 2017-2019 Jarred Vardy, Arraying
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
            Gravity gravity = DataContainer.INSTANCE.getGravity();
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
