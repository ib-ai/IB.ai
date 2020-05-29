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
import com.ibdiscord.pagination.Pagination;
import com.ibdiscord.utils.UInput;
import de.arraying.gravity.Gravity;
import de.arraying.gravity.data.property.Property;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;

import java.util.List;
import java.util.stream.Collectors;

public final class HelperList implements CommandAction {

    /**
     * Creates list of helpers.
     * @param context The command context.
     */
    @Override
    public void accept(CommandContext context) {

        context.assertArguments(1, "error.generic_syntax_arg");

        if (context.getArguments()[0].equals("inactive")) {

            Gravity gravity = DataContainer.INSTANCE.getGravity();
            HelperInactiveData helperInactiveData = gravity.load(new HelperInactiveData(context.getGuild().getId()));

            List<String> helperIds = helperInactiveData.values().stream()
                    .map(Property::toString)
                    .collect(Collectors.toList());

            paginateEmbed(context, helperIds, null,true);
            return;
        }

        String desiredRole = context.getArguments()[0];
        Role roleFinal = UInput.getRole(context.getGuild(), desiredRole);

        if (roleFinal == null) {
            if (context.getMessage().getMentionedChannels().size() > 0) {
                TextChannel mentionedChannel = context.getMessage().getMentionedChannels().get(0);

                PermissionOverride rolePermissionOverride = mentionedChannel.getRolePermissionOverrides()
                        .stream()
                        .filter(permissionOverride -> permissionOverride.getRole()
                                .getName().toLowerCase().endsWith("helper"))
                        .findFirst()
                        .orElse(null);

                if (rolePermissionOverride == null) {
                    context.replyI18n("error.helper_list_channel");
                    return;
                }

                roleFinal = rolePermissionOverride.getRole();
            } else {
                desiredRole = desiredRole + " Helper";
                roleFinal = UInput.getRole(context.getGuild(), desiredRole);

                if (roleFinal == null) {
                    context.replyI18n("error.helper_list_incorrect");
                    return;
                }
            }
        }

        if (!roleFinal.getName().toLowerCase().endsWith("helper")) {
            context.replyI18n("error.helper_list_incorrect");
            return;
        }

        List<String> helperIds = context.getGuild().getMembersWithRoles(roleFinal).stream()
                .map(Member::getId)
                .collect(Collectors.toList());

        paginateEmbed(context, helperIds, roleFinal, false);
    }

    private void paginateEmbed(CommandContext context, List<String> helpers, Role role, boolean inactive) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        if (!inactive) {
            StringBuilder helpersString = new StringBuilder();

            String subject = role.getName().split("Helper")[0].trim();
            embedBuilder.setTitle(String.format("Helper for %s", subject));
            helpers.forEach(member -> helpersString.append(String.format("<@%s>", member)).append("\n"));
            embedBuilder.addField("Your subject helpers for this subject are:", helpersString.toString(), false);
        } else {
            embedBuilder.setTitle("List Of Inactive Helpers");
            Pagination<String> pagination = new Pagination<>(helpers, 10);

            int page = 1;
            if(context.getArguments().length >= 2) {
                try {
                    page = Integer.parseInt(context.getArguments()[1]);
                } catch (NumberFormatException ex) {
                    // Ignored
                }
            }

            pagination.page(page).forEach(entry -> {
                Member member = context.getGuild().getMemberById(entry.getValue());

                String embedFieldTitle = (String.format("%s (%s)", member.getUser().getAsTag(), entry.getValue()));

                String roles = member.getRoles().stream()
                        .filter(memberRole -> memberRole.getName().toLowerCase().endsWith("helper"))
                        .map(Role::getAsMention)
                        .collect(Collectors.joining(", "));

                embedBuilder.addField(
                        embedFieldTitle,
                        roles,
                        false
                );
            });

            if (embedBuilder.getFields().size() == 0) {
                embedBuilder.setDescription(
                        context.__(context, "error.helper_404")
                );
            }

            embedBuilder.setFooter(
                    context.__(context, "info.paginated",String.valueOf(page), String.valueOf(pagination.total())),
                    null
            );
        }

        context.replyEmbed(embedBuilder.build());
    }
}
