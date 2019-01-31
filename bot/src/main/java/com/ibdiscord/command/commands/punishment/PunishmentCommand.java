package com.ibdiscord.command.commands.punishment;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.Option;
import com.ibdiscord.command.permissions.CommandPermission;
import com.ibdiscord.data.db.DContainer;
import com.ibdiscord.data.db.entries.GuildData;
import com.ibdiscord.data.db.entries.punish.PunishmentData;
import com.ibdiscord.data.db.entries.punish.PunishmentsData;
import com.ibdiscord.utils.UFormatter;
import com.ibdiscord.utils.UInput;
import com.ibdiscord.utils.UString;
import com.ibdiscord.utils.UTime;
import de.arraying.gravity.Gravity;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.HashSet;
import java.util.Set;

import static com.ibdiscord.command.commands.ModLogCommand.DISABLED_MOD_LOG;
import static com.ibdiscord.data.db.entries.punish.PunishmentData.*;

/**
 * Copyright 2018 Arraying
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
public abstract class PunishmentCommand extends Command {

    /**
     * An infinite or undefined punishment duration.
     */
    public static final long DURATION_INDEFINITELY = -1;

    private final String display;

    /**
     * Creates a new abstract punishment command.
     * @param name The name of the command.
     * @param aliases The aliases.
     * @param permission The permission.
     * @param display The display name for the punishment type.
     */
    protected PunishmentCommand(String name, Set<String> aliases, CommandPermission permission, String display) {
        super(name, aliases, permission, new HashSet<>());
        this.display = display;
    }

    /**
     * Executes this punishment. Each punishment type should implement this
     * as the actual "punishing" part, e.g. the command that is executed to ban a user.
     * The implementation should take care of any errors that occur, as well as any
     * criteria that is not met for the punishment (e.g. prints out that there is no permission).
     * @param member The member to punish.
     * @param reason The reason for the punishment. Implementing this is optional, as the
     * reason will be logged anyways if mod logs are enabled. If there is no reason specified,
     * then null will be passed in.
     * @param duration The duration that this punishment should last, in milliseconds.
     * If the punishment does not involve duration, this parameter can be safely ignored.
     * If the punishment is indefinite, {@link #DURATION_INDEFINITELY} will be passed.
     * @return Whether or not the punishment was successful. Please ensure that this
     * will always return a value that is as correct as possible.
     */
    protected abstract boolean punish(Member member, String reason, long duration);

    /**
     * Executes the punishment command.
     * @param context The context.
     */
    @Override
    protected final void execute(CommandContext context) {
        String[] args = context.getArguments();
        if(args.length == 0) {
            context.reply("Please provide a member that you would like to punish, as well as a reason.");
            return;
        }
        Member member = UInput.getMember(context.getGuild(), args[0]);
        Member staff = context.getMember();
        if(member == null) {
            context.reply("The member that you wanted to punish does not exist.");
            return;
        }
        long duration = DURATION_INDEFINITELY;
        Option option = context.getOptions().stream()
                .filter(it -> it.getName().equalsIgnoreCase("duration") && it.getValue() != null)
                .findFirst()
                .orElse(null);
        if(option != null) {
            String durationRaw = option.getValue();
            long time = UTime.parseDuration(durationRaw);
            if(time != -1) {
                duration = time - System.currentTimeMillis();
            }
        }
        String reason = args.length > 1 ? UString.concat(args, " ", 1) : null;
        boolean result = punish(member, reason, duration);
        if(!result) {
            context.reply("Something went wrong while punishing. Are the permissions correct?");
            return;
        }
        String guildId = context.getGuild().getId();
        Gravity gravity = DContainer.INSTANCE.getGravity();
        PunishmentsData punishmentList = gravity.load(new PunishmentsData(guildId));
        long caseNumber = punishmentList.size() + 1;
        punishmentList.add(caseNumber);
        PunishmentData punishmentData = gravity.load(new PunishmentData(guildId, caseNumber));
        String userDisplay = UFormatter.formatMember(member);
        String userId = member.getUser().getId();
        String staffDisplay = UFormatter.formatMember(staff);
        String staffId = staff.getUser().getId();
        punishmentData.set(CASE, caseNumber);
        punishmentData.set(USER_DISPLAY, userDisplay);
        punishmentData.set(USER_ID, userId);
        punishmentData.set(STAFF_DISPLAY, staffDisplay);
        punishmentData.set(STAFF_ID, staffId);
        GuildData guildData = gravity.load(new GuildData(guildId));
        long modLogChannel = guildData
                .get(GuildData.MODLOGS)
                .defaulting(DISABLED_MOD_LOG)
                .asLong();
        if(modLogChannel != DISABLED_MOD_LOG) {
            TextChannel textChannel = context.getGuild().getTextChannelById(modLogChannel);
            if(textChannel != null) {
                textChannel.sendMessage(UFormatter.makeAModLog(
                        caseNumber,
                        display,
                        userDisplay,
                        userId,
                        staffDisplay,
                        staffId,
                        null
                )).queue(wellPlayedSir -> punishmentData.set(MESSAGE, wellPlayedSir.getId()));
            }
        }
        gravity.save(punishmentData);
    }

}
