/* Copyright 2020 Nathaneal Varghese
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

import com.ibdiscord.command.CommandAction;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.helper.HelperInactiveData;
import com.ibdiscord.utils.UString;
import de.arraying.gravity.Gravity;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.util.concurrent.atomic.AtomicBoolean;

public class HelperInactive implements CommandAction {

    /**
     * Sets helper as inactive.
     * @param context The command context.
     */
    @Override
    public void accept(CommandContext context) {

        long memberId;
        AtomicBoolean helperRole = new AtomicBoolean(false);

        Gravity gravity = DataContainer.INSTANCE.getGravity();
        HelperInactiveData helperInactiveData = gravity.load(new HelperInactiveData(context.getGuild().getId()));

        if (context.getArguments().length < 1) {
            EmbedBuilder inactiveHelperList = new EmbedBuilder();
            inactiveHelperList.setTitle("List Of Inactive Helpers");

            StringBuilder allInactiveHelpers = new StringBuilder();

            helperInactiveData.values().forEach(helperId -> {
                Member helper = context.getGuild().getMemberById(helperId.asLong());
                StringBuilder allHelperRoles = new StringBuilder();
                helper.getRoles().forEach(role -> {
                    String[] words = role.getName().split(" ");
                    for (int i = 0; i < words.length; i++) {
                        if ((words[i].toLowerCase().equals(" helper") || words[i].toLowerCase().equals("helper"))
                                && i == 1) {
                            allHelperRoles.append(role.getName()).append(", ");
                        }
                    }
                });
                String value = UString.truncate(allHelperRoles.substring(0, allHelperRoles.length() - 2), 512);
                allInactiveHelpers.append(String.format("<@%s>", helperId))
                        .append(" (").append(value).append(") ").append("\n");
            });

            inactiveHelperList.setDescription(allInactiveHelpers);
            context.getChannel().sendMessage(inactiveHelperList.build()).queue();
            return;
        }

        try {
            memberId = Long.valueOf(context.getArguments()[0]);
        } catch(NumberFormatException exception) {
            context.replyI18n("error.reaction_invalidid");
            return;
        }

        try {

            StringBuilder allHelperRoles = new StringBuilder();

            context.getGuild().getMemberById(memberId).getRoles().forEach(role -> {
                String[] words = role.getName().split(" ");
                for (int i = 0; i < words.length; i++) {
                    if ((words[i].toLowerCase().equals(" helper") || words[i].toLowerCase().equals("helper"))
                            && i == 1) {

                        allHelperRoles.append(role.getName()).append(", ");
                        helperRole.set(true);

                    }
                }
            });

            if (!helperRole.get()) {
                context.replyI18n("error.helper_list_incorrect");
                return;
            }

            String value = UString.truncate(allHelperRoles.substring(0, allHelperRoles.length() - 2), 512);

            if (!helperInactiveData.contains(memberId)) {
                helperInactiveData.add(memberId);
                context.replyI18n("success.helper_inactive",
                        String.format("<@%s>", memberId), value);
            } else {
                helperInactiveData.remove(memberId);
                context.replyI18n("success.helper_active",
                        String.format("<@%s>", memberId), value);
            }

            gravity.save(helperInactiveData);

        } catch (NullPointerException e) {
            context.replyI18n("error.helper_list_incorrect");
        }
    }
}
