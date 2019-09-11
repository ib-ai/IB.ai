/* Copyright 2017-2019 Arraying
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

package com.ibdiscord.command.permissions;

import com.ibdiscord.IBai;
import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.GuildData;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;

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
     * Creates a new per-guild role permission.
     * @param field The key which will get a value of a role ID or role name.
     * @return A permission.
     */
    public static CommandPermission role(String field) {
        return new CommandPermission(PermissionType.ROLE, field);
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
    public boolean hasPermission(Member member, GuildChannel channel) {
        switch(type) {
            case DISCORD:
                return member.hasPermission(channel, (Permission) value);
            case ROLE: {
                String data = DataContainer.INSTANCE.getGravity().load(new GuildData(channel.getGuild().getId()))
                        .get(value.toString())
                        .asString();
                return (data != null && member.getRoles().stream()
                        .anyMatch(it -> it.getName().toLowerCase()
                            .contains(data.toLowerCase()) || it.getId().equalsIgnoreCase(data)))
                            || member.hasPermission(Permission.ADMINISTRATOR);
            }
            case DEVELOPER:
                return IBai.INSTANCE.getConfig().getDevelopIDs().contains(member.getUser().getIdLong())
                        && (value == null || ((CommandPermission) value).hasPermission(member, channel));
            default:
                break;
        }
        throw new IllegalStateException("Permission not exhaustive");
    }

}
