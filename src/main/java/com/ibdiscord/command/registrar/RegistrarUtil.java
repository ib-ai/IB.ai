/* Copyright 2017-2020 Arraying
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

package com.ibdiscord.command.registrar;

import com.ibdiscord.IBai;
import com.ibdiscord.command.Command;
import com.ibdiscord.command.actions.LangList;
import com.ibdiscord.command.actions.ReminderList;
import com.ibdiscord.command.registry.CommandRegistrar;
import com.ibdiscord.command.registry.CommandRegistry;
import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.GuildUserData;
import com.ibdiscord.data.db.entries.LangData;
import com.ibdiscord.i18n.EmbedBuilderI18n;
import com.ibdiscord.i18n.Locale;
import com.ibdiscord.i18n.LocaliserHandler;
import com.ibdiscord.i18n.StringI18n;
import com.ibdiscord.input.InputHandler;
import com.ibdiscord.input.embed.EmbedDescriptionInput;
import com.ibdiscord.reminder.Reminder;
import com.ibdiscord.reminder.ReminderHandler;
import com.ibdiscord.utils.UDatabase;
import com.ibdiscord.utils.UString;
import de.arraying.gravity.Gravity;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public final class RegistrarUtil implements CommandRegistrar {

    /**
     * Registers commands.
     * @param registry The command registry.
     */
    @Override
    public void register(CommandRegistry registry) {
        registry.define("avatar")
                .on(context -> {
                    Member target = context.assertMemberArgument("error.user_404");

                    context.replyRaw(target.getUser().getEffectiveAvatarUrl() + "?size=1024"); // Nice and big.
                });

        registry.define("embed")
                .on(context -> InputHandler.INSTANCE.start(context.getMember(), new EmbedDescriptionInput(), context));

        registry.define("help")
                .on(context -> {
                    String botPrefix = UDatabase.getPrefix(context.getGuild());
                    List<TextChannel> channelsGetRoles =
                            context.getGuild().getTextChannelsByName("get-roles", true);
                    String getRoles =
                            channelsGetRoles.isEmpty() ? "#get-roles" : channelsGetRoles.get(0).getAsMention();
                    List<TextChannel> channelsJoinLounge =
                            context.getGuild().getTextChannelsByName("join-lounge", true);
                    String joinLounge =
                            channelsJoinLounge.isEmpty() ? "#join-lounge" : channelsJoinLounge.get(0).getAsMention();

                    EmbedBuilderI18n embedBuilder = new EmbedBuilderI18n(context)
                            .setColor(Color.white)
                            .setAuthor(new StringI18n(null, "IB.ai"),
                                    IBai.INSTANCE.getConfig().getGithubLink())
                            .setDescription(new StringI18n("info.intro_welcome",
                                    IBai.INSTANCE.getConfig().getBotVersion()))
                            .addField(new StringI18n("info.intro_started1"),
                                    new StringI18n("info.intro_started2", getRoles, joinLounge),
                                    false)
                            .addField(new StringI18n("info.intro_features1"),
                                    new StringI18n("info.intro_features2", botPrefix),
                                    false)
                            .addField(new StringI18n("info.intro_dev1"),
                                    new StringI18n("info.intro_dev2",
                                            IBai.INSTANCE.getConfig().getGithubLink(),
                                            IBai.INSTANCE.getConfig().getBotAuthors().stream()
                                                    .map(it -> "- " + it)
                                                    .collect(Collectors.joining("\n"))),
                                    false)
                            .setFooter(new StringI18n("info.intro_footer"));
                    context.replyEmbed(embedBuilder.build());
                });

        Command commandLang = registry.define("lang")
                .sub(registry.sub("list", "generic_list")
                        .on(new LangList())
                )
                .sub(registry.sub("set", null)
                        .on(context -> {
                            context.assertArguments(1, "generic_syntax_arg");
                            String language = context.getArguments()[0].toLowerCase();
                            Collection<Locale> locales = LocaliserHandler.INSTANCE.locales();
                            Locale locale = locales.stream()
                                    .filter(it -> it.getCode().equalsIgnoreCase(language)
                                            || it.getName().equalsIgnoreCase(language))
                                    .findFirst()
                                    .orElse(null);
                            if(locale == null) {
                                context.replyI18n("error.lang_command",
                                        language,
                                        UDatabase.getPrefix(context.getGuild())
                                );
                            } else {
                                Gravity gravity = DataContainer.INSTANCE.getGravity();
                                LangData langData = gravity.load(new LangData());
                                langData.set(context.getMember().getUser().getId(), locale.getCode());
                                gravity.save(langData);
                                context.replyI18n("success.lang_command");
                            }
                        })
                );
        commandLang.on(context -> context.replySyntax(commandLang));

        // Needs a noinspection because checkstyle is annoying with lambda expressions.
        // Why do we even need a program to tell us how to factor our code?
        // Surely it'd be more effective to have code reviews and spank contributors' butts if they don't adhere
        // by the coding standards.

        //noinspection CodeBlock2Expr
        registry.define("ping")
                .on(context -> {
                    context.replyI18n("info.latency",
                            context.getJda().getRestPing().complete(),
                            context.getJda().getGatewayPing());
                });

        Command commandReminder = registry.define("reminder")
                .sub(registry.sub("create", "generic_create")
                        .on(context -> {
                            context.assertArguments(1, "error.reminder_duration");
                            context.assertArguments(2, "error.reminder_content");
                            long duration = context.assertDuration(context.getArguments()[0],
                                    "error.reminder_format");
                            String reminder = UString.concat(context.getArguments(), " ", 1);
                            ReminderHandler.INSTANCE.create(context.getMember().getUser(), duration, reminder);
                            context.replyI18n("success.reminder_schedule");
                        })
                )
                .sub(registry.sub("delete", "generic_delete")
                        .on(context -> {
                            context.assertArguments(1, "error.reminderid");
                            List<Reminder> reminders = ReminderHandler.INSTANCE.getFor(context.getMember().getUser())
                                    .stream()
                                    .filter(it -> !it.isCompleted())
                                    .collect(Collectors.toList());
                            String id = context.getArguments()[0];
                            Reminder reminder = reminders.stream()
                                    .filter(it -> !it.isCompleted())
                                    .filter(it -> String.valueOf(it.getId()).equals(id))
                                    .findFirst()
                                    .orElse(null);
                            if(reminder == null) {
                                context.replyI18n("error.reminder_inactiveid");
                                return;
                            }
                            reminder.setCompleted(true);
                            context.replyI18n("success.reminder_delete");
                        })
                )
                .sub(registry.sub("list", "generic_list")
                        .on(new ReminderList())
                );
        commandReminder.on(context -> context.replySyntax(commandReminder));

        registry.define("serverinfo")
                .on(context -> {
                    Guild guild = context.getGuild();

                    @SuppressWarnings("ConstantConditions")
                    EmbedBuilderI18n embedBuilder = new EmbedBuilderI18n(context)
                            .setAuthor(new StringI18n(null, guild.getName(),
                                    IBai.INSTANCE.getConfig().getGithubLink(), guild.getIconUrl()))
                            .addField(new StringI18n("info.owner"),
                                    new StringI18n(null, guild.getOwner().getUser().getAsTag()),
                                    true
                            )
                            .addField(new StringI18n("info.creation_date"),
                                    new StringI18n(null,
                                            guild.getTimeCreated().format(DateTimeFormatter.RFC_1123_DATE_TIME)),
                                    true
                            )
                            .addField(new StringI18n("info.vc_region"),
                                    new StringI18n(null, guild.getRegion().getName()),
                                    true
                            )
                            .addField(new StringI18n("info.number_members"),
                                    new StringI18n(null, guild.getMembers().size()),
                                    true
                            )
                            .addField(new StringI18n("info.number_bots"),
                                    new StringI18n(null, guild.getMembers().stream()
                                            .filter(it -> it.getUser().isBot())
                                            .count()),
                                    true
                            )
                            .addField(new StringI18n("info.number_online"),
                                    new StringI18n(null, guild.getMembers().stream()
                                            .filter(it -> it.getOnlineStatus() != OnlineStatus.OFFLINE)
                                            .count()),
                                    true
                            )
                            .addField(new StringI18n("info.number_roles"),
                                    new StringI18n(null, guild.getRoles().size()),
                                    true
                            )
                            .addField(new StringI18n("info.number_channels"),
                                    new StringI18n(null, guild.getVoiceChannels().size()
                                            + guild.getTextChannels().size()
                                            + guild.getCategories().size()),
                                    true
                            );
                    context.replyEmbed(embedBuilder.build());
                });

        registry.define("userinfo")
                .on(context -> {
                    Member target = context.assertMemberArgument("error.user_404");
                    User user = target.getUser();
                    int joinPosition = context.getGuild().getMembers().stream()
                            .sorted((o1, o2) -> {
                                long a = o1.getTimeJoined().toInstant().toEpochMilli();
                                long b = o2.getTimeJoined().toInstant().toEpochMilli();
                                return Long.compare(a, b);
                            })
                            .collect(Collectors.toList())
                            .indexOf(target) + 1;
                    String joinOverride = DataContainer.INSTANCE.getGravity()
                            .load(new GuildUserData(context.getGuild().getId(), user.getId()))
                            .get("position")
                            .asString();
                    Activity activity = target.getActivities().size() == 0 ? null : target.getActivities().get(0);

                    EmbedBuilderI18n embedBuilder = new EmbedBuilderI18n(context)
                            .setAuthor(new StringI18n(null, user.getAsTag()),
                                    "https://discord.gg/ibo",
                                    user.getEffectiveAvatarUrl()
                            )
                            .addField(new StringI18n(null, "ID"),
                                    new StringI18n(null, user.getId()),
                                    true
                            )
                            .addField(new StringI18n("info.user_nick"),
                                    new StringI18n(null, target.getEffectiveName()),
                                    true
                            )
                            .addField(new StringI18n("info.user_status"),
                                    new StringI18n(null, target.getOnlineStatus().toString()),
                                    true
                            )
                            .addField(new StringI18n("info.user_game"),
                                    new StringI18n(null, activity == null ? "-" : activity.getName()),
                                    true
                            )
                            .addField(new StringI18n("info.user_joined"),
                                    new StringI18n(null,
                                            target.getTimeJoined().format(DateTimeFormatter.RFC_1123_DATE_TIME)),
                                    true
                            )
                            .addField(new StringI18n("info.user_position"),
                                    new StringI18n(null, joinOverride == null ? joinPosition : joinOverride),
                                    true
                            )
                            .addField(new StringI18n("info.user_registered"),
                                    new StringI18n(null,
                                            user.getTimeCreated().format(DateTimeFormatter.RFC_1123_DATE_TIME)),
                                    true
                            );
                    context.replyEmbed(embedBuilder.build());
                });

        registry.define("userroles")
                .on(context -> {
                    Member target = context.assertMemberArgument("error.user_404");

                    context.replyI18n("info.user_roles", target.getRoles().stream()
                            .map(Role::getName)
                            .collect(Collectors.joining(", ")));
                });
    }

}
