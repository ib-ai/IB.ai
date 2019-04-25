package com.ibdiscord.listeners;

import com.ibdiscord.IBai;
import com.ibdiscord.data.db.DContainer;
import com.ibdiscord.data.db.entries.GuildData;
import com.ibdiscord.data.db.entries.RoleData;
import com.ibdiscord.punish.Punishment;
import com.ibdiscord.punish.PunishmentHandler;
import com.ibdiscord.punish.PunishmentType;
import com.ibdiscord.utils.UFormatter;
import de.arraying.gravity.Gravity;
import net.dv8tion.jda.core.audit.AuditLogChange;
import net.dv8tion.jda.core.audit.AuditLogEntry;
import net.dv8tion.jda.core.audit.AuditLogKey;
import net.dv8tion.jda.core.audit.TargetType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.GuildUnbanEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.*;
import java.util.stream.Collectors;

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
public final class GuildListener extends ListenerAdapter {

    /**
     * When a member joins the server.
     * @param event The event instance.
     */
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        Member member = event.getMember();
        Gravity gravity = DContainer.INSTANCE.getGravity();
        RoleData roleData = gravity.load(new RoleData(member.getGuild().getId(), member.getUser().getId()));
        if(roleData == null) {
            return;
        }
        Role highest = event.getGuild().getSelfMember().getRoles().stream()
                .max(Comparator.comparing(Role::getPosition))
                .orElse(null);
        Collection<Role> roles = roleData.values().stream()
                .map(it -> member.getGuild().getRoleById(it.defaulting(0L).asLong()))
                .filter(Objects::nonNull)
                .filter(it -> highest == null || it.getPosition() < highest.getPosition())
                .collect(Collectors.toSet());
        member.getGuild().getController().addRolesToMember(member, roles).queue();
        roleData.delete();
        gravity.save(roleData);
    }

    /**
     * When a member leaves a server.
     * @param event The event instance.
     */
    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        queryAuditLog(event.getGuild(), event.getMember().getUser().getIdLong());
        Gravity gravity = DContainer.INSTANCE.getGravity();
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
        queryAuditLog(event.getGuild(), event.getMember().getUser().getIdLong());
    }

    /**
     * When a role is removed from a member.
     * @param event The event.
     */
    @Override
    public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event) {
        queryAuditLog(event.getGuild(), event.getMember().getUser().getIdLong());
    }

    /**
     * When someone is unbanned.
     * @param event The event.
     */
    @Override
    public void onGuildUnban(GuildUnbanEvent event) {
        queryAuditLog(event.getGuild(), event.getUser().getIdLong());
    }

    /**
     * Queries the audit logs to gain information about punishment handling.
     * @param guild The guild.
     * @param target The target member.
     */
    private synchronized void queryAuditLog(Guild guild, long target) {
        guild.getAuditLogs().queue(entries -> {
            if(entries.isEmpty()) {
                return;
            }
            AuditLogEntry latest = entries.get(0);
            IBai.INSTANCE.getLogger().info("Latest audit log: type '{}', target type '{}', target id '{}', user {}, options {}",
                    latest.getType(),
                    latest.getTargetType(),
                    latest.getTargetId(),
                    latest.getUser(),
                    latest.getOptions());
            if(latest.getTargetType() != TargetType.MEMBER
                    || latest.getTargetIdLong() != target) {
                IBai.INSTANCE.getLogger().info("Test 1: {}, Test 2: {}",
                        (latest.getTargetType() != TargetType.MEMBER),
                        (latest.getTargetIdLong() != target));
                return;
            }
            guild.getJDA().retrieveUserById(latest.getTargetIdLong()).queue(user -> {
                User staff = latest.getUser();
                String reason = latest.getReason();
                if(user == null
                        || staff == null) {
                    throw new RuntimeException("user/staff nil");
                }
                Punishment punishment = new Punishment(null,
                        UFormatter.formatMember(user),
                        user.getId(),
                        UFormatter.formatMember(staff),
                        staff.getId(),
                        reason,
                        false
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
                        punishment.setType(PunishmentType.BAN);
                        handler.onPunish();
                        break;
                    case UNBAN:
                        punishment.setType(PunishmentType.BAN);
                        handler.onRevocation();
                        break;
                }
            });
        }, error -> {
            IBai.INSTANCE.getLogger().info("Blimey, there's been an error.");
            error.printStackTrace();
        });
    }

    /**
     * Whether or not a list of role IDs contains the muted role for the guild.
     * @param guild The guild.
     * @param roles A list of roles.
     * @return True if it does, false otherwise.
     */
    private boolean isNotMute(Guild guild, List<Map<String, String>> roles) {
        String id = DContainer.INSTANCE.getGravity().load(new GuildData(guild.getId())).get(GuildData.MUTE)
                .defaulting("")
                .asString();
        for(Map<String, String> entry : roles) {
            if(entry.get("id").equalsIgnoreCase(id)) {
                return false;
            }
        }
        return true;
    }

}
