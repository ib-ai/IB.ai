/**
 * Copyright 2018 raynichc
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

/**
 * @author raynichc
 * @since 2018.11.29
 */

package com.ibdiscord.command.commands;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.DContainer;
import com.ibdiscord.data.db.entries.BannedUserData;
import com.ibdiscord.data.db.entries.BansData;
import com.ibdiscord.data.db.entries.BotPrefixData;
import com.ibdiscord.data.db.entries.ModLogData;
import com.ibdiscord.main.IBai;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.User;

import java.util.HashSet;

public final class KickCommand extends Command {

    public KickCommand() {
        super("kick",
            new HashSet<>(),
            CommandPermission.discord(Permission.KICK_MEMBERS),
            new HashSet<>()
        );
    }

    @Override
    protected void execute(CommandContext context) {
        String botPrefix = DContainer.getGravity().load(new BotPrefixData(context.getGuild().getId())).get().defaulting(IBai.getConfig().getStaticPrefix()).asString();
        if (context.getArguments().length == 0) {
            context.reply("Correct usage: `" + botPrefix + "Kick @User` or `" + botPrefix + "Kick [UserID]`");
            return;
        }

        User userToKick;

        if(!context.getMessage().getMentionedUsers().isEmpty()) {
            userToKick = context.getMessage().getMentionedUsers().get(0);
        } else {
            try {
                userToKick = context.getGuild().getMemberById(context.getArguments()[0]).getUser();
            } catch (Exception e) {
                //TODO: Write invalid input message
                return;
            }
        }

        context.getGuild().getController().kick(userToKick.getId()).queue();

        ModLogData modLog = DContainer.getGravity().load(new ModLogData(context.getGuild().getId()));
        if(modLog.get().defaulting("000").asString() != "000") {
            BansData bans = DContainer.getGravity().load(new BansData(context.getGuild().getId()));

            String caseNumber = String.valueOf(bans.size());

            String kickedUserName = userToKick.getName();
            String kickedUserID = userToKick.getId();

            String modUserName = context.getMember().getUser().getName();
            String modUserID = context.getMember().getUser().getId();

            BannedUserData bannedUser = new BannedUserData(kickedUserID);
            bannedUser.set("ban_num", caseNumber);
            bannedUser.set("userID", kickedUserID);
            bannedUser.set("moderatorID", modUserID);

            Long modLogChannelID = modLog.get().asLong();

            String kickMessage = "**Case: #" + caseNumber + " | Kick :boot:**\n" +
                    "**Offender: **" + kickedUserName + " (ID: " + kickedUserID + ")\n" +
                    "**Moderator: **" + modUserName + " (ID: " + modUserID + ")\n" +
                    "**Reason: ** Use `" + botPrefix + "Reason [Case Number]` to append a reason.";

            context.getGuild().getTextChannelById(modLogChannelID).sendMessage(kickMessage).queue(owo ->
                    bannedUser.set("banLogID", owo.getId())
            );
            DContainer.getGravity().save(bannedUser);
        }

    }
}