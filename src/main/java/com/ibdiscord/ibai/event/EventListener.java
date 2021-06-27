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

package com.ibdiscord.ibai.event;

import discord4j.core.event.domain.Event;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public interface EventListener<T extends Event> {

    Logger log = LoggerFactory.getLogger(EventListener.class);

    /**
     * Gets the type of event that this event listener will handle.
     * @return The class of the event type.
     */
    Class<T> getType();

    /**
     * Executes the event.
     * @param event The event.
     * @return Void when completion is done.
     */
    Mono<Void> execute(T event);

    /**
     * Default handler for errors.
     * @param throwable The throwable.
     * @return An empty mono.
     */
    default Mono<Void> error(@NonNull Throwable throwable) {
        log.error("Error parsing event", throwable);
        return Mono.empty();
    }
}
