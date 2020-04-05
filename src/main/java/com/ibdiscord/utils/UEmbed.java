/* Copyright 2020 Ray Clark
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

package com.ibdiscord.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;

public final class UEmbed {

    /**
     *  Generates a Helper Message Embed based on which roles is given.
     * @param guild The guild.
     * @param role The helper role.
     * @return A populated Helper Message embed.
     */
    public static MessageEmbed helperMessageEmbed(Guild guild, Role role) {
        EmbedBuilder builder = new EmbedBuilder();
        String subject = role.getName().split("Helper")[0].trim();
        builder.setDescription(String.format("**__Helpers for %s__**", subject));
        StringBuilder helpers = new StringBuilder();
        guild.getMembersWithRoles(role).forEach(member ->
                helpers.append(String.format("<@%s>", member.getId())).append("\n")
        );
        builder.addField("Your subject helpers for this subject are:", helpers.toString(), false);

        builder.addField(
                "*What are 'subject helpers'?*",
                "Subject helpers, or simply \"helpers\", are members who volunteer their time and expertise to "
                        + "help fellow members with certain subjects. You can contact the Helpers for this subject by "
                        + "tagging the [subject helper role] role. Please wait 15mins after your question is posted "
                        + "before doing so, though. Most channels & helpers are fairly active anyway, so you should "
                        + "see your question(s) answered before then, whether by a helper or someone else.",
                false
        );

        builder.addField(
                "*How do I become a Helper?*",
                "To apply to become a Helper, please fill out this form: "
                        + "<https://forms.gle/UhRmZdUJNDFgPJgeA>\n\nYour application will be reviewed by the Helper "
                        + "Managers. Once a decision's been made, you'll be notified via DMs.",
                false
        );
        return builder.build();
    }
}
