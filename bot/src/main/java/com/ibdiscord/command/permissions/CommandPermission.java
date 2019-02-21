package com.ibdiscord.command.permissions;

import com.ibdiscord.IBai;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Member;

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
@SuppressWarnings("unused")
public final class CommandPermission {

    private final PermissionType type;
    private final Object value;

    /**
     * Creates a new Discord permission (everyone can use).
     * @return A permission.
     */
    public static CommandPermission discord() {
        return new CommandPermission(PermissionType.DISCORD, Permission.MESSAGE_WRITE);
    }

    /**
     * Creates a new Discord permission.
     * @param permission The Discord permission.
     * @return A permission.
     */
    public static CommandPermission discord(Permission permission) {
        return new CommandPermission(PermissionType.DISCORD, permission);
    }

    /**
     * Creates a new role permission.
     * @deprecated Hard coding is not the solution.
     * @param id The ID of the role.
     * @return A permission.
     */
    @Deprecated
    public static CommandPermission roleId(long id) {
        return new CommandPermission(PermissionType.ROLE_ID, id);
    }

    /**
     * Creates a new role permission.
     * @deprecated Hard coding is not the solution.
     * @param name The name of the role.
     * @return A permission.
     */
    @Deprecated
    public static CommandPermission roleName(String name) {
        return new CommandPermission(PermissionType.ROLE_NAME, name);
    }

    /**
     * Creates a new developer only permission, with an extra permission on top of that.
     * @param onTopOfThat The extra permission.
     * @return A permission.
     */
    public static CommandPermission developer(CommandPermission onTopOfThat) {
        return new CommandPermission(PermissionType.DEVELOPER, onTopOfThat);
    }

    /**
     * Creates the permission.
     * @param type The type of permission.
     * @param value The permission value.
     */
    private CommandPermission(PermissionType type, Object value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Whether or not the member has permission.
     * @param member The member.
     * @param channel The channel.
     * @return True if they do, false otherwise.
     */
    public boolean hasPermission(Member member, Channel channel) {
        switch(type) {
            case DISCORD:
                return member.hasPermission(channel, (Permission) value);
            case ROLE_ID:
                return member.getRoles().stream().anyMatch(it -> it.getIdLong() == (long) value);
            case ROLE_NAME:
                return member.getRoles().stream().anyMatch(it -> it.getName().equals(value));
            case DEVELOPER:
                return IBai.INSTANCE.getConfig().getDevelopIDs().contains(member.getUser().getIdLong())
                        && (value == null || ((CommandPermission) value).hasPermission(member, channel));
        }
        throw new IllegalStateException("Permission not exhaustive");
    }

}
