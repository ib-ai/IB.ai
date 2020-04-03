/* Copyright 2017-2020 Arraying
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

package com.ibdiscord.command.actions;

import com.ibdiscord.IBai;
import com.ibdiscord.command.CommandAction;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.OptData;
import com.ibdiscord.utils.UInput;
import com.ibdiscord.utils.UString;
import de.arraying.gravity.Gravity;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.PermissionOverride;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class Opt implements CommandAction {

    /**
     * Opts in or out of a channel.
     * @param context The command context.
     */
    @Override
    public void accept(CommandContext context) {
        String userId = context.getMember().getUser().getId();
        Gravity gravity = DataContainer.INSTANCE.getGravity();
        OptData optData = gravity.load(new OptData(context.getGuild().getId(), userId));

        if(context.getArguments().length < 1) {
            String channels = optData.values().stream()
                    .map(it -> it.defaulting(0).asLong()) // Get all IDs as longs.
                    .map(it -> context.getGuild().getGuildChannelById(it)) // Get the channel object.
                    .filter(Objects::nonNull) // Remove invalid channels.
                    .map(GuildChannel::getName) // Get channel name.
                    .collect(Collectors.joining(", ")); // Join name.
            channels = channels.isEmpty() ? "N/A" : channels;
            context.replyI18n("info.opt", channels);
            return;
        }

        String input = UString.concat(context.getArguments(), " ", 0);
        GuildChannel channel = UInput.getChannelGuild(context.getGuild(), input, true);
        if(channel == null) {
            context.replyI18n("error.opt_channel");
            return;
        }

        List<Long> categories = IBai.INSTANCE.getConfig().getOptCategories();
        List<Long> blacklist = IBai.INSTANCE.getConfig().getOptBlacklist();
        if(blacklist.contains(channel.getIdLong())
                || (channel.getParent() != null && !categories.contains(channel.getParent().getIdLong()))) {
            context.replyI18n("error.opt_disabled");
            return;
        }

        List<PermissionOverride> overrides = channel.getPermissionOverrides();
        if(!optData.contains(channel.getIdLong())) { // Does not contain.
            // Check if the person can actually view the channel in the first place.
            if(!context.getMember().hasPermission(channel, Permission.MESSAGE_READ)) {
                context.replyI18n("error.opt_permission");
                return;
            }
            // Check for existing overrides - disable this for security reasons.
            if(overrides.stream().anyMatch(override -> override.isMemberOverride()
                    && override.getMember() != null
                    && override.getMember().getUser().getId().equalsIgnoreCase(userId))) {
                context.replyI18n("error.opt_override");
                return;
            }
            optData.add(channel.getIdLong());
            override(channel, context.getMember(), false);
            context.replyI18n("success.opt_out");
        } else { // Contains.
            optData.remove(channel.getIdLong());
            override(channel, context.getMember(), true);
            context.replyI18n("success.opt_in");
        }
        gravity.save(optData);
    }

    /**
     * Handles the channel permission override for a member.
     * @param channel The channel.
     * @param member The member.
     * @param allow True to allow the member to see the channel, false otherwise.
     */
    public static void override(GuildChannel channel, Member member, boolean allow) {
        if(channel == null
                || member == null) { // Should not happen, just in case.
            return;
        }
        if(!allow) {
            channel.putPermissionOverride(member)
                    .deny(Permission.MESSAGE_READ)
                    .queue();
        } else {
            PermissionOverride override = channel.getPermissionOverride(member);
            if(override != null) {
                override.delete().queue();
            }
        }
    }

}
