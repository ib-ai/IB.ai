package com.ibdiscord.command.permissions;

import com.ibdiscord.main.IBai;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Member;

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
@SuppressWarnings("unused")
public final class CommandPermission {

    private final PermissionType type;
    private final Object value;

    public static CommandPermission discord() {
        return new CommandPermission(PermissionType.DISCORD, Permission.MESSAGE_WRITE);
    }

    public static CommandPermission discord(Permission permission) {
        return new CommandPermission(PermissionType.DISCORD, permission);
    }

    public static CommandPermission roleId(long id) {
        return new CommandPermission(PermissionType.ROLE_ID, id);
    }

    public static CommandPermission roleName(String name) {
        return new CommandPermission(PermissionType.ROLE_NAME, name);
    }

    public static CommandPermission developer(CommandPermission onTopOfThat) {
        return new CommandPermission(PermissionType.DEVELOPER, onTopOfThat);
    }

    private CommandPermission(PermissionType type, Object value) {
        this.type = type;
        this.value = value;
    }

    public boolean hasPermission(Member member, Channel channel) {
        switch(type) {
            case DISCORD:
                return member.hasPermission(channel, (Permission) value);
            case ROLE_ID:
                return member.getRoles().stream().anyMatch(it -> it.getIdLong() == (long) value);
            case ROLE_NAME:
                return member.getRoles().stream().anyMatch(it -> it.getName().equals(value));
            case DEVELOPER:
                return IBai.getConfig().getDevelopIDs().contains(member.getUser().getIdLong())
                        && (value == null || ((CommandPermission) value).hasPermission(member, channel));
        }
        throw new IllegalStateException("permission not exhaustive");
    }
}
