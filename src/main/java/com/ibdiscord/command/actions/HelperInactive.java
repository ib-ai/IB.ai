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
import com.ibdiscord.i18n.LocaleShorthand;
import com.ibdiscord.utils.UInput;
import de.arraying.gravity.Gravity;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.util.stream.Collectors;

public class HelperInactive implements CommandAction, LocaleShorthand {

    /**
     * Sets helper as inactive.
     * @param context The command context.
     */
    @Override
    public void accept(CommandContext context) {

        context.assertArguments(1, "generic_syntax_arg");

        Gravity gravity = DataContainer.INSTANCE.getGravity();
        HelperInactiveData helperInactiveData = gravity.load(new HelperInactiveData(context.getGuild().getId()));

        Member member = UInput.getMember(context.getGuild(), context.getArguments()[0]);

        if (member == null) {
            context.replyI18n("error.reaction_invalidid");
            return;
        }

        String roles = member.getRoles().stream()
                .map(Role::getName)
                .filter(roleName -> roleName.toLowerCase().endsWith("helper"))
                .collect(Collectors.joining(", "));

        if (roles.isEmpty()) {
            context.replyI18n("error.helper_list_incorrect");
            return;
        }

        if (!helperInactiveData.contains(member.getId())) {
            helperInactiveData.add(member.getId());
            context.replyI18n("success.helper_inactive",
                    String.format("<@%s>", member.getId()), roles);
        } else {
            helperInactiveData.remove(member.getId());
            context.replyI18n("success.helper_active",
                    String.format("<@%s>", member.getId()), roles);
        }

        gravity.save(helperInactiveData);

    }
}
