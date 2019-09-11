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

import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.FilterData;
import com.ibdiscord.data.db.entries.GuildData;
import com.ibdiscord.utils.objects.GuildedCache;
import de.arraying.gravity.Gravity;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

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
        Gravity gravity = DataContainer.INSTANCE.getGravity();
        GuildData guildData = gravity.load(new GuildData(event.getGuild().getId()));
        FilterData filterData = gravity.load(new FilterData(event.getGuild().getId()));
        if(!guildData.get(GuildData.FILTERING).defaulting(false).asBoolean()) {
            return;
        }
        if(filterData.values().stream()
                .map(it -> filterCache.compute(event.getGuild().getIdLong(),
                        it.asString(),
                        Pattern.compile(it.asString()))
                )
                .anyMatch(it -> it.matcher(message).matches())) {
            event.getMessage().delete().queue(success ->
                event.getAuthor().openPrivateChannel().queue(dm -> {
                    String send = String.format("The following message has been automatically flagged and "
                            + "removed from %s:\n\n%s", event.getGuild().getName(), message);
                    send = send.length() > 2000 ? send.substring(0, 2000) : send;
                    dm.sendMessage(send).queue();
                })
            );
        }
    }

}
