package com.ibdiscord.listeners;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.audit.ActionType;
import net.dv8tion.jda.core.audit.AuditLogEntry;
import net.dv8tion.jda.core.events.guild.GuildBanEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

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
     * When a member joins a server.
     * @param event The event instance.
     */
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        //TODO: I don't know what to do with this
        //TODO: Check if the user is muted? Maybe it might be a good idea to use sticky mutes?
    }

    /**
     * When a member leaves a server.
     * This also includes if they are kicked, because sending separate kick events
     * alongside ban events for consistency is completely overrated and an insane,
     * ridiculous idea. I hate developers.
     * @param event The event instance.
     */
    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        var guild = event.getGuild();
        var user = event.getUser();
        if(guild.getSelfMember().hasPermission(Permission.VIEW_AUDIT_LOGS)) {
            guild.getAuditLogs().queue(entries -> {
                if(entries.isEmpty()) {
                    return;
                }
                AuditLogEntry first = entries.get(0);
                if(first.getUser() == null
                        || first.getTargetIdLong() != user.getIdLong()
                        || first.getType() != ActionType.KICK
                        || first.getUser().getIdLong() == guild.getSelfMember().getUser().getIdLong()) {
                    //noinspection UnnecessaryReturnStatement
                    return;
                }
                //TODO: it's a kick here
            });
        }
        //TODO: it's a regular leave here
    }

    /**
     * When a member is banned from the server.
     * @param event The event instance.
     */
    @Override
    public void onGuildBan(GuildBanEvent event) {
        //TODO: I don't know what to do with this
    }

}
