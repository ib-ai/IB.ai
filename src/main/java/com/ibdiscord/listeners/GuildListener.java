/* Copyright 2018-2020 Arraying
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

import com.ibdiscord.IBai;
import com.ibdiscord.command.actions.Opt;
import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.GuildData;
import com.ibdiscord.data.db.entries.OptData;
import com.ibdiscord.data.db.entries.RoleData;
import com.ibdiscord.data.db.entries.helper.HelperMessageData;
import com.ibdiscord.data.db.entries.helper.HelperMessageRolesData;
import com.ibdiscord.punish.Punishment;
import com.ibdiscord.punish.PunishmentHandler;
import com.ibdiscord.punish.PunishmentType;
import com.ibdiscord.utils.UEmbed;
import de.arraying.gravity.Gravity;
import net.dv8tion.jda.api.audit.ActionType;
import net.dv8tion.jda.api.audit.AuditLogChange;
import net.dv8tion.jda.api.audit.AuditLogEntry;
import net.dv8tion.jda.api.audit.AuditLogKey;
import net.dv8tion.jda.api.audit.TargetType;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildUnbanEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public final class GuildListener extends ListenerAdapter {

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private final Object mutex = new Object();

    /**
     * When a member joins the server.
     * @param event The event instance.
     */
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        Member member = event.getMember();
        Gravity gravity = DataContainer.INSTANCE.getGravity();

        OptData optData = gravity.load(new OptData(member.getGuild().getId(), member.getUser().getId()));
        optData.values().stream()
                .map(it -> it.defaulting(0).asLong())
                .map(it -> member.getGuild().getGuildChannelById(it))
                .filter(Objects::nonNull)
                .filter(it -> it.getType() == ChannelType.TEXT || it.getType() == ChannelType.VOICE)
                .forEach(it -> Opt.override(it, member, false));

        RoleData roleData = gravity.load(new RoleData(member.getGuild().getId(), member.getUser().getId()));
        if(roleData == null) {
            return;
        }
        Role highest = event.getGuild().getSelfMember().getRoles().stream()
                .max(Comparator.comparing(Role::getPosition))
                .orElse(null);
        List<Role> roles = roleData.values().stream()
                .map(it -> member.getGuild().getRoleById(it.defaulting(0L).asLong()))
                .filter(Objects::nonNull)
                .filter(it -> highest == null || it.getPosition() < highest.getPosition()) // Avoid permission error
                .filter(it -> IBai.INSTANCE.getConfig().getSensitiveRoles().stream()
                        .noneMatch(sensitive -> it.getIdLong() == sensitive) // Do not give sensitive roles back
                )
                .collect(Collectors.toList());
        roles.addAll(member.getRoles());
        member.getGuild().modifyMemberRoles(member, roles).queue();
        roleData.delete();
        gravity.save(roleData);
    }

    /**
     * When a member leaves a server.
     * @param event The event instance.
     */
    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        updateHelperMessages(event.getGuild(), event.getMember().getRoles());
        IBai.INSTANCE.getLogger().info("Looking for Kick or Ban for user: '{}'", event.getMember().getUser().getIdLong());
        queryAuditLog(event.getGuild(), event.getMember().getUser().getIdLong(), ActionType.BAN, ActionType.KICK);
        Gravity gravity = DataContainer.INSTANCE.getGravity();
        RoleData roleData = gravity.load(new RoleData(event.getGuild().getId(), event.getMember().getUser().getId()));
        for(Role role : event.getMember().getRoles()) {
            roleData.add(role.getId());
        }
        gravity.save(roleData);
    }

    /**
     * When a role is added to a member.
     * @param event The event.
     */
    @Override
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
        updateHelperMessages(event.getGuild(), event.getRoles());
        queryAuditLog(event.getGuild(), event.getMember().getUser().getIdLong(), ActionType.MEMBER_ROLE_UPDATE);
    }

    /**
     * When a role is removed from a member.
     * @param event The event.
     */
    @Override
    public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event) {
        updateHelperMessages(event.getGuild(), event.getRoles());
        queryAuditLog(event.getGuild(), event.getMember().getUser().getIdLong(), ActionType.MEMBER_ROLE_UPDATE);
    }

    /**
     * When someone is unbanned.
     * @param event The event.
     */
    @Override
    public void onGuildUnban(GuildUnbanEvent event) {
        queryAuditLog(event.getGuild(), event.getUser().getIdLong(), ActionType.UNBAN);
    }

    /**
     * Queries the audit logs to gain information about punishment handling.
     * @param guild The guild.
     * @param target The target member.
     */
    private void queryAuditLog(Guild guild, long target, ActionType... actionTypes) {
        executorService.schedule(() -> {
            synchronized(mutex) {
                guild.retrieveAuditLogs().limit(10).queue(entries -> {
                    if(entries.isEmpty()) {
                        IBai.INSTANCE.getLogger().info("No audit logs retrieved.");
                        return;
                    }

                    for (AuditLogEntry latest : entries) {
                        if (Arrays.stream(actionTypes).anyMatch(latest.getType()::equals)
                                && latest.getTargetType() == TargetType.MEMBER
                                && latest.getTargetIdLong() == target) {

                            IBai.INSTANCE.getLogger().info("Found audit log: type '{}', target type '{}', target id '{}', "
                                            + "user {}, options {}",
                                    latest.getType(),
                                    latest.getTargetType(),
                                    latest.getTargetId(),
                                    latest.getUser(),
                                    latest.getOptions());

                            guild.getJDA().retrieveUserById(latest.getTargetIdLong()).queue(user -> {
                                User staff = latest.getUser();
                                String reason = latest.getReason();
                                if(user == null
                                        || staff == null) {
                                    IBai.INSTANCE.getLogger().info("User or Staff is null");
                                    throw new RuntimeException("user/staff nil");
                                }
                                boolean redacted = false;
                                if(reason != null
                                        && (reason.toLowerCase().contains("-redacted")
                                        || reason.toLowerCase().contains("-redact"))) {
                                    redacted = true;
                                    reason = reason.replace("-redacted", "").replace("-redact", "");
                                }
                                //Temporary Shorthand for rule 5 bans
                                if (reason != null && reason.equalsIgnoreCase("-r5")) {
                                    reason = "Rule 5. Academic Dishonesty is strictly prohibited.";
                                    redacted = true;
                                }
                                Punishment punishment = new Punishment(null,
                                        user.getAsTag(),
                                        user.getId(),
                                        staff.getAsTag(),
                                        staff.getId(),
                                        reason,
                                        redacted
                                );
                                PunishmentHandler handler = new PunishmentHandler(guild, punishment);
                                switch(latest.getType()) {
                                    case KICK:
                                        punishment.setType(PunishmentType.KICK);
                                        handler.onPunish();
                                        break;
                                    case MEMBER_ROLE_UPDATE:
                                        AuditLogChange rolesAddedRaw = latest.getChangeByKey(AuditLogKey.MEMBER_ROLES_ADD);
                                        AuditLogChange rolesRemovedRaw = latest.getChangeByKey(AuditLogKey.MEMBER_ROLES_REMOVE);
                                        if(rolesAddedRaw != null) {
                                            if(staff.getIdLong() == guild.getJDA().getSelfUser().getIdLong()) {
                                                IBai.INSTANCE.getLogger().info("Ignored adding role since it was sent by user "
                                                                + "{} (self)",
                                                        staff.getIdLong()
                                                );
                                                return;
                                            }
                                            List<Map<String, String>> roles = rolesAddedRaw.getNewValue();
                                            if(isNotMute(guild, roles)) {
                                                return;
                                            }
                                            punishment.setType(PunishmentType.MUTE);
                                            handler.onPunish();
                                        }
                                        if(rolesRemovedRaw != null) {
                                            List<Map<String, String>> roles = rolesRemovedRaw.getNewValue();
                                            if(isNotMute(guild, roles)) {
                                                return;
                                            }
                                            punishment.setType(PunishmentType.MUTE);
                                            handler.onRevocation();
                                        }
                                        break;
                                    case BAN:
                                        IBai.INSTANCE.getLogger().info("Handling Ban");
                                        punishment.setType(PunishmentType.BAN);
                                        handler.onPunish();
                                        break;
                                    case UNBAN:
                                        punishment.setType(PunishmentType.BAN);
                                        handler.onRevocation();
                                        break;
                                    default:
                                        break;
                                }
                            }, error -> {
                                IBai.INSTANCE.getLogger().info("Could not retrieve user.");
                                IBai.INSTANCE.getLogger().info(error.getMessage());
                            });
                            break;
                        }
                    }
                }, error -> {
                        IBai.INSTANCE.getLogger().info("Blimey, there's been an error in the audit log.");
                        IBai.INSTANCE.getLogger().error(error.getMessage());
                    }
                );
            }
        }, 3, TimeUnit.SECONDS);
    }

    /**
     * Whether or not a list of role IDs contains the muted role for the guild.
     * @param guild The guild.
     * @param roles A list of roles.
     * @return True if it does, false otherwise.
     */
    private boolean isNotMute(Guild guild, List<Map<String, String>> roles) {
        if(roles == null) {
            return false;
        }
        String id = DataContainer.INSTANCE.getGravity().load(new GuildData(guild.getId())).get(GuildData.MUTE)
                .defaulting("")
                .asString();
        for(Map<String, String> entry : roles) {
            if(entry.get("id").equalsIgnoreCase(id)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Updates the Helper Messages if someone is helpered or unhelpered.
     */
    private void updateHelperMessages(Guild guild, List<Role> roles) {
        HelperMessageRolesData helperMessageRolesData = DataContainer.INSTANCE.getGravity().load(
                new HelperMessageRolesData(guild.getId())
        );

        roles.forEach(role -> {
            if (helperMessageRolesData.contains(role.getId())) {
                HelperMessageData helperMessageData = DataContainer.INSTANCE.getGravity().load(
                        new HelperMessageData(guild.getId(), role.getId())
                );

                helperMessageData.getKeys().stream().forEach(channelId -> {
                    TextChannel channel = guild.getTextChannelById(channelId);
                    if (channel == null) {
                        return;
                    }
                    channel.editMessageById(helperMessageData.get(channelId).asString(),
                            UEmbed.helperMessageEmbed(guild, role)).queue();
                });
            }
        });
    }
}
