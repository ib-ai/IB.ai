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

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class ButtonListener extends ListenerAdapter {

    /**
     * Adds the respective member to the role when the interaction is triggered.
     *
     * @param event The event.
     */
    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        if (event.getMessage() == null) return;
        Message message = event.getMessage();
        Guild guild = event.getGuild();
        if (message.getAuthor().getIdLong() != message.getJDA().getSelfUser().getIdLong() || guild == null) return;
        String customId = event.getComponentId();
        if (customId.isEmpty()) return;
        Member member = event.getMember();
        if (member == null) return;

        List<Role> toAdd = new ArrayList<>();
        for (String roleId : customId.split(",")) {
            Role role = guild.getRoleById(roleId);
            if (role != null) toAdd.add(role);
        }
        event.deferReply(true).queue(deferred ->
                guild.modifyMemberRoles(member, toAdd, new ArrayList<>()).queue(yes ->
                        event.getHook().sendMessage(generateMessage(toAdd.size() > 0, false))
                .queue(), Throwable::printStackTrace), Throwable::printStackTrace);
    }

    /**
     * Generates a message.
     *
     * @param added   The number of roles added.
     * @param removed The number of roles removed.
     * @return A message.
     */
    private String generateMessage(boolean added, boolean removed) {
        String content;
        if (added && !removed) content = "Added " + added + " role(s).";
        else if (removed && !added) content = "Removed " + removed + " role(s).";
        else if (added) content = "Added " + added + " role(s) and removed " + removed + " role(s).";
        else content = "Did not update roles";
        return content;
    }
}
