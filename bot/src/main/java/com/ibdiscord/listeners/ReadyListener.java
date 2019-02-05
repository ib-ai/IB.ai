package com.ibdiscord.listeners;

import com.ibdiscord.IBai;
import com.ibdiscord.data.db.DContainer;
import com.ibdiscord.data.db.entries.punish.ExpiryData;
import com.ibdiscord.punish.Punishment;
import com.ibdiscord.punish.PunishmentExpiry;
import net.dv8tion.jda.bot.entities.ApplicationInfo;
import net.dv8tion.jda.core.entities.Guild;
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

        ApplicationInfo appInfo = event.getJDA().asBot().getApplicationInfo().complete();
        String botName = appInfo.getName();
        String botOwner = appInfo.getOwner().getName();
        String botDescription = appInfo.getDescription();
        boolean isPublicBot = appInfo.isBotPublic();
        int guildNum = event.getJDA().getGuilds().size();
        Logger logger = IBai.INSTANCE.getLogger();
        logger.info("Bot \"{}\" by \"{}\" is now connected.", botName, botOwner);
        logger.info("Currently serving {} guilds.", guildNum);
        logger.info("Described as \"{}\", {}.", botDescription, (isPublicBot ? "public" : "private"));
        for(Guild guild : event.getJDA().getGuilds()) {
            ExpiryData expiryData = DContainer.INSTANCE.getGravity().load(new ExpiryData(guild.getId()));
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
    }

}
