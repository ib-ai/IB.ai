package com.ibdiscord.command.commands;

import com.ibdiscord.IBai;
import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.DContainer;
import com.ibdiscord.data.db.entries.GuildData;
import com.ibdiscord.data.db.entries.punish.PunishmentData;
import com.ibdiscord.data.db.entries.punish.PunishmentsData;
import com.ibdiscord.punish.Punishment;
import com.ibdiscord.punish.PunishmentHandler;
import com.ibdiscord.utils.UString;
import de.arraying.gravity.Gravity;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.HashSet;
import java.util.Set;

import static com.ibdiscord.data.db.entries.punish.PunishmentData.*;

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
public final class ReasonCommand extends Command {

    /**
     * Creates the command.
     */
    public ReasonCommand() {
        super("reason",
                Set.of(),
                CommandPermission.role(GuildData.MODERATOR),
                new HashSet<>()
        );
        this.correctUsage = "reason <reason>";
    }

    /**
     * Sets the reason.
     * @param context The context.
     */
    @Override
    protected void execute(CommandContext context) {
        if(context.getArguments().length < 2) {
            sendUsage(context);
            return;
        }
        Guild guild = context.getGuild();
        String caseNumber = context.getArguments()[0];
        String reason = UString.concat(context.getArguments(), " ", 1);
        Gravity gravity = DContainer.INSTANCE.getGravity();
        PunishmentsData punishmentList = gravity.load(new PunishmentsData(guild.getId()));
        if(!punishmentList.contains(caseNumber)) {
            context.reply("That case does not exist!");
            return;
        }
        Long caseId = UString.toLong(caseNumber);
        if(caseId == null) {
            context.reply("Internal error converting to long.");
            return;
        }
        Punishment punishment = Punishment.of(context.getGuild(), caseId);
        punishment.setReason(reason);
        PunishmentData punishmentData = gravity.load(new PunishmentData(guild.getId(), caseId));
        PunishmentHandler punishmentHandler = new PunishmentHandler(guild, punishment);
        TextChannel channel = punishmentHandler.getLogChannel();
        if(channel == null) {
            context.reply("Logging is currently not enabled or set up incorrectly.");
            return;
        }
        boolean redacted = context.getOptions().stream()
                .anyMatch(it -> it.getName().equalsIgnoreCase("redacted") || it.getName().equalsIgnoreCase("redact"));
        IBai.INSTANCE.getLogger().info("Redacting: " + redacted);
        punishmentData.set(REASON, reason);
        punishmentData.set(REDACTED, redacted);
        punishment.redacting(redacted);
        gravity.save(punishmentData);
        channel.editMessageById(punishmentData.get(MESSAGE).defaulting(0L).asLong(), punishment.getLogPunishment(guild, caseId)).queue(
                outstandingMove -> {
                    punishmentData.set(REASON, reason);
                    context.reply("The reason has been updated.");
                },
                error -> {
                    context.reply("An error occurred, the reason could not be updated.");
                    error.printStackTrace();
                }
        );
    }

}
