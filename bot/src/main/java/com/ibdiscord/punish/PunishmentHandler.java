package com.ibdiscord.punish;

import com.ibdiscord.data.db.DContainer;
import com.ibdiscord.data.db.entries.GuildData;
import com.ibdiscord.data.db.entries.punish.PunishmentData;
import com.ibdiscord.data.db.entries.punish.PunishmentsData;
import de.arraying.gravity.Gravity;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;

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
public @AllArgsConstructor class PunishmentHandler {

    private final Guild guild;
    private final Punishment punishment;

    /**
     * Executes the punishment and sends the message.
     */
    public void onPunish() {
        Gravity gravity = DContainer.INSTANCE.getGravity();
        long caseNumber = gravity.load(new PunishmentsData(guild.getId())).size() + 1;
        punishment.dump(guild, caseNumber);
        TextChannel channel = getLogChannel();
        if(channel == null) {
            return;
        }
        channel.sendMessage(punishment.getLogPunishment(guild, caseNumber)).queue(success -> {
            PunishmentData punishmentData = gravity.load(new PunishmentData(guild.getId(), caseNumber));
            punishmentData.set(PunishmentData.MESSAGE, success.getIdLong());
            gravity.save(punishmentData);
        });
    }

    /**
     * Executes a revocation.
     * When doing this, the data inside the punishment can be null.
     * The only required data is the type, user display/id, staff display/id.
     */
    public void onRevocation() {
        TextChannel channel = getLogChannel();
        if(channel != null) {
            channel.sendMessage(punishment.getLogRevocation()).queue();
        }
    }

    /**
     * Gets the log channel.
     * @return The possibly null log channel.
     */
    public TextChannel getLogChannel() {
        GuildData guildData = DContainer.INSTANCE.getGravity().load(new GuildData(guild.getId()));
        return guild.getTextChannelById(guildData.get(GuildData.MODLOGS)
                .defaulting(0L)
                .asLong());
    }

}
