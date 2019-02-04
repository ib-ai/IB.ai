package com.ibdiscord.command.commands.punishment;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.DContainer;
import com.ibdiscord.data.db.entries.punish.PunishmentData;
import com.ibdiscord.data.db.entries.punish.PunishmentsData;
import com.ibdiscord.punish.PunishmentHandler;
import com.ibdiscord.punish.PunishmentWrapper;
import com.ibdiscord.utils.UString;
import de.arraying.gravity.Gravity;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.HashSet;
import java.util.Set;

import static com.ibdiscord.data.db.entries.punish.PunishmentData.MESSAGE;
import static com.ibdiscord.data.db.entries.punish.PunishmentData.REASON;

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
                CommandPermission.roleId(230710782038376449L),
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
        }
        String caseNumber = context.getArguments()[0];
        String reason = UString.concat(context.getArguments(), "", 1);
        Gravity gravity = DContainer.INSTANCE.getGravity();
        PunishmentsData punishmentList = gravity.load(new PunishmentsData(context.getGuild().getId()));
        if(!punishmentList.contains(caseNumber)) {
            context.reply("That case does not exist!");
            return;
        }
        long caseId;
        try {
            caseId = Long.valueOf(caseNumber);
        } catch(NumberFormatException exception) {
            exception.printStackTrace();
            context.reply("Error with case number, report this to a developer.");
            return;
        }
        PunishmentData punishmentData = gravity.load(new PunishmentData(context.getGuild().getId(), caseId));
        PunishmentWrapper wrapper = PunishmentWrapper.of(context.getGuild().getId(), caseId);
        wrapper.setReason(reason);
        PunishmentHandler handler = new PunishmentHandler(context.getGuild(), wrapper);
        TextChannel channel = handler.getModLogChannel();
        if(channel != null) {
            channel.editMessageById(punishmentData.get(MESSAGE).defaulting(0).asLong(), handler.getMogLogMessage(caseId)).queue(
                    outstandingMove -> {
                        context.reply("The reason has been updated.");
                        punishmentData.set(REASON, reason);
                    },
                    error -> context.reply("The reason could not be updated.")
            );
        } else {
            context.reply("Logging is currently not enabled; could not update the reason.");
        }
        gravity.save(punishmentData);
    }

}
