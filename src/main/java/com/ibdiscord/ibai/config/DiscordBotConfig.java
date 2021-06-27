/* Copyright 2017-2021 Arraying
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

package com.ibdiscord.ibai.config;

import com.ibdiscord.ibai.event.EventListener;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
public class DiscordBotConfig {

    @Value("${discord.token}") private String token;

    @Bean
    public <T extends Event> GatewayDiscordClient client(List<EventListener<T>> list) {
        log.info("Initialized Discord client");
        GatewayDiscordClient client = DiscordClientBuilder.create(token)
            .build()
            .login()
            .block();
        if (client == null) {
            throw new NullPointerException("Discord client could not initialize, null");
        }
        try {
            for (EventListener<T> eventListener : list) {
                client.on(eventListener.getType())
                    .flatMap(eventListener::execute)
                    .onErrorResume(eventListener::error)
                    .subscribe();
            }
        } catch (NullPointerException exception) {
            log.error("Error registering event listeners", exception);
        }
        return client;
    }

}
