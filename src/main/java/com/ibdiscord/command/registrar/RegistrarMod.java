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
import com.ibdiscord.data.db.entries.monitor.MonitorData;
import com.ibdiscord.data.db.entries.monitor.MonitorMessageData;
import com.ibdiscord.data.db.entries.monitor.MonitorUserData;
import com.ibdiscord.data.db.entries.voting.VoteLadderData;
import com.ibdiscord.data.db.entries.voting.VoteLaddersData;
import com.ibdiscord.utils.UInput;
import com.ibdiscord.utils.UString;
import com.ibdiscord.vote.VoteEntry;
import com.ibdiscord.vote.VoteLadder;
import de.arraying.gravity.Gravity;
import de.arraying.gravity.data.property.Property;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;
import java.util.stream.Collectors;

public final class RegistrarMod implements CommandRegistrar {

    @Override
    public void register(CommandRegistry registry) {
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
                );
        commandFilter.on(context -> context.replySyntax(commandFilter));
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

        Command commandVote = registry.define("voteladder")
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
    }

}
