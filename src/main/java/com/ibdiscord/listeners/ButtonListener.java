/* Copyright 2017-2021 Arraying
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

import com.ibdiscord.button.ButtonCallbackAction;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.RawGatewayEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.data.DataObject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class ButtonListener extends ListenerAdapter {

    /**
     * Adds the respective member to the role when the interaction is triggered.
     * @param event The event.
     */
    @Override
    public void onRawGateway(@NotNull RawGatewayEvent event) {
        if (event.getType().equals("INTERACTION_CREATE")) {
            DataObject payload = event.getPayload();
            DataObject member = payload.getObject("member");
            DataObject data = payload.getObject("data");
            String author = payload.getObject("message").getObject("author").getString("id");
            if (!author.equals(event.getJDA().getSelfUser().getId())) {
                return;
            }
            String customId = data.getString("custom_id");
            String memberId = member.getObject("user").getString("id");
            String guildId = payload.getString("guild_id");
            Guild guild = event.getJDA().getGuildById(guildId);
            if (guild == null) {
                return;
            }
            Member guildMember = guild.getMemberById(memberId);
            if (guildMember == null) {
                return;
            }
            List<Role> toAdd = new ArrayList<>();
            for (String roleId : customId.split(",")) {
                Role role = guild.getRoleById(roleId);
                if (role != null) {
                    toAdd.add(role);
                }
            }
            guild.modifyMemberRoles(guildMember, toAdd, null).queue(yes -> {
                new ButtonCallbackAction(event.getJDA(), payload.getString("id"), payload.getString("token"), toAdd.size(), 0).queue();
            });
        }
    }
}
