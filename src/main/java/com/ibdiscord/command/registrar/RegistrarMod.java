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
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.abstractions.MonitorManage;
import com.ibdiscord.command.abstractions.VoteLadderManage;
import com.ibdiscord.command.actions.*;
import com.ibdiscord.command.permission.CommandPermission;
import com.ibdiscord.command.registry.CommandRegistrar;
import com.ibdiscord.command.registry.CommandRegistry;
import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.GuildData;
import com.ibdiscord.data.db.entries.NoteData;
import com.ibdiscord.data.db.entries.monitor.MonitorData;
import com.ibdiscord.data.db.entries.monitor.MonitorMessageData;
import com.ibdiscord.data.db.entries.monitor.MonitorUserData;
import com.ibdiscord.data.db.entries.punish.ExpiryData;
import com.ibdiscord.data.db.entries.punish.PunishmentData;
import com.ibdiscord.data.db.entries.punish.PunishmentsData;
import com.ibdiscord.data.db.entries.voting.VoteLadderData;
import com.ibdiscord.data.db.entries.voting.VoteLaddersData;
import com.ibdiscord.pagination.Pagination;
import com.ibdiscord.punish.Punishment;
import com.ibdiscord.punish.PunishmentExpiry;
import com.ibdiscord.punish.PunishmentHandler;
import com.ibdiscord.utils.UInput;
import com.ibdiscord.utils.UString;
import com.ibdiscord.utils.objects.Tuple;
import com.ibdiscord.vote.VoteEntry;
import com.ibdiscord.vote.VoteLadder;
import de.arraying.gravity.Gravity;
import de.arraying.gravity.data.property.Property;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

import static com.ibdiscord.data.db.entries.punish.PunishmentData.*;

public final class RegistrarMod implements CommandRegistrar {

    /**
     * Registers commands.
     * @param registry The command registry.
     */
    @Override
    public void register(CommandRegistry registry) {
        registry.define("blacklist")
                .restrict(CommandPermission.discord(Permission.BAN_MEMBERS))
                .on(context -> {
                    context.assertArguments(1, "error.blacklist_id");
                    long id = context.assertLong(context.getArguments()[0],
                            null,
                            null,
                            "error.blacklist_id");
                    if(context.getGuild().getMemberById(id) != null) { // Don't ban present users.
                        context.replyI18n("error.blacklist_present");
                        return;
                    }
                    context.getGuild().ban(String.valueOf(id), 0).reason("Blacklisted.").queue(
                        success -> context.replyI18n("success.blacklist"),
                        fail -> context.replyI18n("error.blacklist_fail")
                    );
                });

        registry.define("expire")
                .restrict(CommandPermission.role(GuildData.MODERATOR))
                .on(context -> {
                    context.assertArguments(1, "error.expire_case");
                    context.assertArguments(2, "error.expire_duration");
                    Gravity gravity = DataContainer.INSTANCE.getGravity();
                    String guildId = context.getGuild().getId();
                    String caseNumber = context.getArguments()[0];
                    long expires = context.assertDuration(context.getArguments()[1], "error.expire_duration");
                    long delay = expires - System.currentTimeMillis();
                    if(delay <= 0) {
                        context.replyI18n("error.expire_expired");
                        return;
                    }
                    PunishmentsData list = gravity.load(new PunishmentsData(guildId));
                    if(!list.contains(caseNumber)) {
                        context.replyI18n("error.expire_case");
                        return;
                    }
                    ExpiryData expiryData = gravity.load(new ExpiryData(guildId));
                    Punishment punishment = Punishment.of(context.getGuild(), caseNumber);
                    ScheduledFuture<?> scheduled = PunishmentExpiry.INSTANCE.getFor(caseNumber);
                    if(scheduled != null) {
                        scheduled.cancel(true);
                    }
                    PunishmentExpiry.INSTANCE.schedule(context.getGuild(), caseNumber, delay, punishment);
                    expiryData.set(caseNumber, expires);
                    gravity.save(expiryData);
                    context.replyI18n("success.expire");
                });

        Command commandFilter = registry.define("filter")
                .restrict(CommandPermission.role(GuildData.MODERATOR))
                .sub(registry.sub("create", "generic_create")
                        .on(new FilterCreate())
                )
                .sub(registry.sub("delete", "generic_delete")
                        .on(new FilterDelete())
                )
                .sub(registry.sub("list", "generic_list")
                        .on(new FilterList())
                )
                .sub(registry.sub("toggle", "generic_toggle")
                        .on(new FilterToggle())
                ).sub(registry.sub("notify", "notify")
                        .on(new FilterNotify())
                );
        commandFilter.on(context -> context.replySyntax(commandFilter));

        registry.define("find")
                .restrict(CommandPermission.role(GuildData.MODERATOR))
                .on(context -> {
                    context.assertArguments(1, "error.generic_arg_length");
                    String guild = context.getGuild().getId();
                    String compare = context.getArguments()[0];
                    Gravity gravity = DataContainer.INSTANCE.getGravity();
                    List<Tuple<Punishment, Long>> punishments = new ArrayList<>();
                    long max = gravity.load(new PunishmentsData(guild)).size();
                    for(long i = 1; i <= max; i++) {
                        Punishment punishment = Punishment.of(context.getGuild(), i);
                        if((punishment.getUserId() != null
                                && punishment.getUserId().equalsIgnoreCase(compare))
                                || (punishment.getStaffId() != null
                                && punishment.getStaffId().equalsIgnoreCase(compare))) {
                            punishments.add(new Tuple<>(punishment, i));
                        }
                    }
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    Pagination<Tuple<Punishment, Long>> pagination = new Pagination<>(punishments, 20);
                    int page = 1;
                    if(context.getArguments().length >= 2) {
                        try {
                            page = Integer.valueOf(context.getArguments()[1]);
                        } catch(NumberFormatException ex) {
                            // Ignored
                        }
                    }
                    pagination.page(page).forEach(entry -> {
                        Punishment punishment = entry.getValue().getPropertyA();
                        embedBuilder.addField(
                                context.__(context, "info.case_number", entry.getValue().getPropertyB().toString()),
                                context.__(context, "info.punished_user", punishment.getStaffDisplay(),
                                        punishment.getUserDisplay()), false
                        );
                    });
                    embedBuilder.setFooter(context.__(context, "info.paginated",
                            String.valueOf(page),
                            String.valueOf(pagination.total())), null);
                    context.replyEmbed(embedBuilder.build());
                });

        registry.define("giverole")
                .restrict(CommandPermission.discord(Permission.MANAGE_SERVER))
                .on(context -> {
                    List<Role> roles = context.getMessage().getMentionedRoles();
                    if(roles.isEmpty()) {
                        context.replyI18n("error.role_empty");
                        return;
                    } else if(roles.size() == 1) {
                        context.replyI18n("error.role_missing");
                        return;
                    }

                    Collection<Member> members = context.getGuild().getMembersWithRoles(roles.get(0));
                    members.forEach(member -> context.getGuild().addRoleToMember(member, roles.get(1)).queue());
                    context.replyI18n("success.give_role", members.size());
                });

        registry.define("lookup")
                .restrict(CommandPermission.role(GuildData.MODERATOR))
                .on(context -> {
                    context.assertArguments(1, "error.lookup_noexist");
                    Guild guild = context.getGuild();
                    String caseNumber = context.getArguments()[0];
                    Gravity gravity = DataContainer.INSTANCE.getGravity();
                    PunishmentsData punishmentList = gravity.load(new PunishmentsData(guild.getId()));
                    if(!punishmentList.contains(caseNumber)) {
                        context.replyI18n("error.lookup_noexist");
                        return;
                    }
                    long caseId = context.assertLong(caseNumber,
                            null,
                            null,
                            "error.lookup_convert");
                    Punishment punishment = Punishment.of(context.getGuild(), caseId);
                    context.replyRaw(punishment.redacting(false)
                            .getLogPunishment(context.getGuild(), caseId));
                });

        registry.define("note")
                .restrict(CommandPermission.role(GuildData.MODERATOR))
                .on(context -> {
                    context.assertArguments(1, "error.note_empty");
                    Member member = context.assertMember(context.getArguments()[0], "error.note_invalid");
                    Gravity gravity = DataContainer.INSTANCE.getGravity();
                    NoteData noteData = gravity.load(new NoteData(context.getGuild().getId(),
                            member.getUser().getId()));
                    if(context.getArguments().length == 1) {
                        EmbedBuilder embedBuilder = new EmbedBuilder();
                        embedBuilder.setDescription(context.__(context, "info.note"));
                        noteData.values().stream()
                                .map(it -> it.defaulting("N/A:N/A").toString())
                                .forEach(it -> {
                                    String user;
                                    String data;
                                    int index = it.indexOf(":");
                                    if(index < 0) {
                                        user = "???";
                                        data = it;
                                    } else {
                                        user = it.substring(0, index);
                                        data = it.substring(index);
                                        if(data.length() > 1) {
                                            data = data.substring(1);
                                        }
                                    }
                                    embedBuilder.addField(context.__(context, "info.note_author", user),
                                            data,
                                            false);
                                });
                        context.replyEmbed(embedBuilder.build());
                    } else {
                        String note = UString.concat(context.getArguments(), " ", 1);
                        if(note.length() > MessageEmbed.VALUE_MAX_LENGTH) {
                            context.replyI18n("error.note_long", MessageEmbed.VALUE_MAX_LENGTH);
                            return;
                        }
                        if(noteData.size() >= 25) {
                            context.replyI18n("error.note_full");
                            return;
                        }
                        String data = context.getMember().getUser().getId() + ":" + note;
                        noteData.add(data);
                        gravity.save(noteData);
                        context.replyI18n("success.note");
                    }
                });

        Command commandMonitor = registry.define("monitor")
                .restrict(CommandPermission.role(GuildData.MODERATOR)) // All moderators can use the base command.
                .sub(registry.sub("toggle", "generic_toggle")
                        .restrict(CommandPermission.discord(Permission.MANAGE_SERVER))
                        .on(context -> {
                            Gravity gravity = DataContainer.INSTANCE.getGravity();
                            MonitorData monitorData = gravity.load(new MonitorData(context.getGuild().getId()));
                            boolean current = monitorData.get(MonitorData.ENABLED)
                                    .defaulting(false)
                                    .asBoolean();
                            monitorData.set(MonitorData.ENABLED, !current);
                            gravity.save(monitorData);
                            context.replyI18n(current ? "success.monitor_disable" : "success.monitor_enable");
                        })
                )
                .sub(registry.sub("userchannel", null)
                        .restrict(CommandPermission.discord(Permission.MANAGE_SERVER)) // Manage server only.
                        .on(context -> {
                            context.assertArguments(1, "error.missing_channel");
                            TextChannel channel = context.assertChannel(context.getArguments()[0],
                                    "error.missing_channel");
                            Gravity gravity = DataContainer.INSTANCE.getGravity();
                            MonitorData monitorData = gravity.load(new MonitorData(context.getGuild().getId()));
                            monitorData.set(MonitorData.USER_CHANNEL, channel.getId());
                            gravity.save(monitorData);
                            context.replyI18n("success.done");
                        })
                )
                .sub(registry.sub("user", null)
                        .restrict(CommandPermission.role(GuildData.MODERATOR)) // All moderators.
                        .on(new MonitorManage() {
                            protected boolean isValidInput(CommandContext context, String input) {
                                return UInput.getMember(context.getGuild(), input) != null;
                            }

                            protected void add(CommandContext context, String input) {
                                Gravity gravity = DataContainer.INSTANCE.getGravity();
                                MonitorUserData userData =
                                        gravity.load(new MonitorUserData(context.getGuild().getId()));
                                //noinspection ConstantConditions
                                userData.add(UInput.getMember(context.getGuild(), input).getUser().getId());
                                gravity.save(userData);
                            }

                            protected void remove(CommandContext context, String input) {
                                Gravity gravity = DataContainer.INSTANCE.getGravity();
                                MonitorUserData userData =
                                        gravity.load(new MonitorUserData(context.getGuild().getId()));
                                //noinspection ConstantConditions
                                userData.remove(UInput.getMember(context.getGuild(), input).getUser().getId());
                                gravity.save(userData);
                            }

                            protected List<String> list(CommandContext context) {
                                return DataContainer.INSTANCE.getGravity()
                                        .load(new MonitorUserData(context.getGuild().getId())).values()
                                        .stream()
                                        .map(Property::asString)
                                        .collect(Collectors.toList());
                            }
                        })
                )
                .sub(registry.sub("messagechannel", null)
                        .restrict(CommandPermission.discord(Permission.MANAGE_SERVER)) // Manage server only.
                        .on(context -> {
                            context.assertArguments(1, "error.missing_channel");
                            TextChannel channel = context.assertChannel(context.getArguments()[0],
                                    "error.missing_channel");
                            Gravity gravity = DataContainer.INSTANCE.getGravity();
                            MonitorData monitorData = gravity.load(new MonitorData(context.getGuild().getId()));
                            monitorData.set(MonitorData.MESSAGE_CHANNEL, channel.getId());
                            gravity.save(monitorData);
                            context.replyI18n("success.done");
                        })
                )
                .sub(registry.sub("message", null)
                        .restrict(CommandPermission.role(GuildData.MODERATOR)) // All moderators.
                        .on(new MonitorManage() {
                            protected boolean isValidInput(CommandContext context, String input) {
                                return UInput.isValidRegex(input);
                            }

                            protected void add(CommandContext context, String input) {
                                Gravity gravity = DataContainer.INSTANCE.getGravity();
                                MonitorMessageData messageData =
                                        gravity.load(new MonitorMessageData(context.getGuild().getId()));
                                messageData.add(input);
                                gravity.save(messageData);
                            }

                            protected void remove(CommandContext context, String input) {
                                Gravity gravity = DataContainer.INSTANCE.getGravity();
                                MonitorMessageData messageData =
                                        gravity.load(new MonitorMessageData(context.getGuild().getId()));
                                messageData.remove(input);
                                gravity.save(messageData);
                            }

                            protected List<String> list(CommandContext context) {
                                return DataContainer.INSTANCE.getGravity()
                                        .load(new MonitorMessageData(context.getGuild().getId())).values()
                                        .stream()
                                        .map(Property::asString)
                                        .collect(Collectors.toList());
                            }
                        })
                )
                .sub(registry.sub("list", "generic_list")
                        .restrict(CommandPermission.role(GuildData.MODERATOR)) // All moderators.
                        .on(new MonitorList())
                )
                .sub(registry.sub("cleanup", "monitor_cleanup")
                        .restrict(CommandPermission.discord(Permission.MANAGE_SERVER)) // Manage server only.
                        .on(new MonitorCleanup())
                );
        commandMonitor.on(context -> context.replySyntax(commandMonitor));

        registry.define("purge")
                .restrict(CommandPermission.discord(Permission.MESSAGE_MANAGE))
                .on(context -> {
                    context.assertArguments(1, "error.generic_arg_length");
                    int amount = context.assertInt(context.getArguments()[0],
                            2,
                            100,
                            "error.purge_range");
                    context.getChannel().getHistory().retrievePast(amount).queue(it ->
                            ((TextChannel) context.getChannel()).deleteMessages(it).queue(jollyGood ->
                                            context.replyI18n("success.done"),
                                bloodyHell -> // Rawr, XD.
                                            context.replyRaw("I tried to hard, and got so far, "
                                                    + "but in the end, it didn't even purge.")
                            )
                    );
                });


        registry.define("reason")
                .restrict(CommandPermission.role(GuildData.MODERATOR))
                .on(context -> {
                    context.assertArguments(1, "error.lookup_noexist");
                    Guild guild = context.getGuild();
                    String caseNumber = context.getArguments()[0];
                    String reason = UString.concat(context.getArguments(), " ", 1);
                    Gravity gravity = DataContainer.INSTANCE.getGravity();
                    PunishmentsData punishmentList = gravity.load(new PunishmentsData(guild.getId()));
                    if(!punishmentList.contains(caseNumber)) {
                        context.replyI18n("error.lookup_noexist");
                        return;
                    }
                    long caseId = context.assertLong(caseNumber,
                            null,
                            null,
                            "error.lookup_convert");
                    Punishment punishment = Punishment.of(context.getGuild(), caseId);
                    punishment.setReason(reason);
                    PunishmentData punishmentData = gravity.load(new PunishmentData(guild.getId(), caseId));
                    PunishmentHandler punishmentHandler = new PunishmentHandler(guild, punishment);
                    TextChannel channel = punishmentHandler.getLogChannel();
                    if(channel == null) {
                        context.replyI18n("error.reason_logging");
                        return;
                    }
                    boolean redacted = context.getOptions().stream()
                            .anyMatch(it -> it.getName().equalsIgnoreCase("redacted") || it.getName()
                                    .equalsIgnoreCase("redact"));
                    IBai.INSTANCE.getLogger().info("Redacting: " + redacted);
                    punishmentData.set(REASON, reason);
                    punishmentData.set(REDACTED, redacted);
                    punishment.redacting(redacted);
                    gravity.save(punishmentData);
                    channel.editMessageById(punishmentData.get(MESSAGE).defaulting(0L).asLong(),
                            punishment.getLogPunishment(guild, caseId)).queue(
                            outstandingMove -> {
                                punishmentData.set(REASON, reason);
                                context.replyI18n("success.reason");
                            },
                            error -> {
                                context.replyI18n("error.reason");
                                error.printStackTrace();
                            }
                    );
                });

        registry.define("vote")
                .restrict(CommandPermission.role(GuildData.MODERATOR))
                .on(context -> {
                    context.assertArguments(2, "error.generic_arg_length");
                    String ladder = context.getArguments()[0].toLowerCase();
                    Gravity gravity = DataContainer.INSTANCE.getGravity();
                    VoteLaddersData laddersData = gravity.load(new VoteLaddersData(context.getGuild().getId()));
                    if(!laddersData.contains(ladder)) {
                        context.replyI18n("error.ladder_noexist");
                        return;
                    }
                    String text = UString.concat(context.getArguments(), " ", 1);
                    VoteLadder voteLadder = new VoteLadder(context.getGuild(), ladder);
                    VoteEntry voteEntry = voteLadder.createVote(text);
                    if(voteEntry == null) {
                        context.replyI18n("error.vote_create");
                        return;
                    }
                    context.replyI18n("success.vote_create", voteEntry.getId());
                });

        Command commandVoteLadder = registry.define("voteladder")
                .restrict(CommandPermission.discord(Permission.MANAGE_SERVER))
                .sub(registry.sub("create", "generic_create")
                        .restrict(CommandPermission.discord(Permission.MANAGE_SERVER))
                        .on(context -> {
                            context.assertArguments(1, "error.ladder_name");
                            String ladder = context.getArguments()[0].toLowerCase();
                            Gravity gravity = DataContainer.INSTANCE.getGravity();
                            VoteLaddersData laddersData = gravity.load(new VoteLaddersData(context.getGuild().getId()));
                            laddersData.add(ladder);
                            gravity.save(laddersData);
                            context.replyI18n("success.ladder_create", ladder);
                        })
                )
                .sub(registry.sub("delete", "generic_delete")
                        .restrict(CommandPermission.discord(Permission.MANAGE_SERVER))
                        .on(context -> {
                            context.assertArguments(1, "error.ladder_name");
                            String ladder = context.getArguments()[0].toLowerCase();
                            Gravity gravity = DataContainer.INSTANCE.getGravity();
                            VoteLaddersData laddersData = gravity.load(new VoteLaddersData(context.getGuild().getId()));
                            laddersData.remove(ladder);
                            gravity.save(laddersData);
                            context.replyI18n("success.ladder_delete", ladder);
                        })
                )
                .sub(registry.sub("list", "generic_list")
                        .restrict(CommandPermission.discord(Permission.MANAGE_SERVER))
                        .on(new VoteLadderList())
                )
                .sub(registry.sub("channel",  null)
                        .restrict(CommandPermission.discord(Permission.MANAGE_SERVER))
                        .on(new VoteLadderManage() {
                            protected void handle(CommandContext context, VoteLadderData ladderData) {
                                context.assertArguments(2, "error.missing_channel");
                                TextChannel channel = context.assertChannel(context.getArguments()[1],
                                        "error.missing_channel");
                                ladderData.set(VoteLadderData.CHANNEL, channel.getIdLong());
                                context.replyI18n("success.channel_update");
                            }
                        })
                )
                .sub(registry.sub("duration", null)
                        .restrict(CommandPermission.discord(Permission.MANAGE_SERVER))
                        .on(new VoteLadderManage() {
                            protected void handle(CommandContext context, VoteLadderData ladderData) {
                                context.assertArguments(2, "error.ladder_format");
                                long time = context.assertDuration(context.getArguments()[1],
                                        "error.lader_format");
                                ladderData.set(VoteLadderData.TIMEOUT, time);
                                context.replyI18n("success.ladder_specify");
                            }
                        })
                )
                .sub(registry.sub("threshold", null)
                        .restrict(CommandPermission.discord(Permission.MANAGE_SERVER))
                        .on(new VoteLadderManage() {
                            protected void handle(CommandContext context, VoteLadderData ladderData) {
                                context.assertArguments(2, "error.missing_number");
                                int result = context.assertInt(context.getArguments()[2],
                                        null,
                                        null,
                                        "error.missing_number");
                                ladderData.set(VoteLadderData.THRESHOLD, result);
                                context.replyI18n("success.threshold_update");
                            }
                        })
                );
        commandVoteLadder.on(context -> context.replySyntax(commandVoteLadder));
    }

}
