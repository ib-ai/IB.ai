/* Copyright 2018-2020 Jarred Vardy <vardy@riseup.net>, Arraying
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

import com.ibdiscord.IBai;
import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permission.CommandPermission;
import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.GuildData;
import com.ibdiscord.data.db.entries.ReplyData;
import com.ibdiscord.data.db.entries.tag.TagActiveData;
import com.ibdiscord.data.db.entries.tag.TagData;
import com.ibdiscord.input.InputHandler;
import com.ibdiscord.odds.OddsManager;
import com.ibdiscord.utils.UDatabase;
import com.ibdiscord.utils.UFormatter;
import com.ibdiscord.utils.objects.ExpiringCache;
import com.ibdiscord.utils.objects.GuildedCache;
import com.ibdiscord.utils.objects.MinimalMessage;
import de.arraying.gravity.Gravity;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.lang3.ArrayUtils;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public final class MessageListener extends ListenerAdapter {

    private final GuildedCache<String, Pattern> tagCache = new GuildedCache<>();
    private final ExpiringCache<Long, MinimalMessage> messageCache = new ExpiringCache<>(1, TimeUnit.HOURS);
    private HashMap<String, ArrayList<MinimalMessage>> recentMessages = new HashMap<>();

    /**
     * When an message is sent in a guild channel (because DMs are boring).
     * @param event The event instance.
     */
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        boolean disableReply = disableReply(event);
        if(!event.getAuthor().isBot()
                && event.getMessage().getMentionedMembers().size() == 0
                && event.getMessage().getMentionedRoles().size() == 0
                && !event.getMessage().mentionsEveryone()
                && !disableReply) {
            repeater(event);
        }
        messageCache.put(event.getMessageIdLong(), new MinimalMessage(event.getAuthor().getIdLong(),
                event.getMessage().getContentRaw())
        );
        if(event.getAuthor().isBot()
                || !event.getGuild().getSelfMember().hasPermission(event.getChannel(), Permission.MESSAGE_WRITE)) {
            return;
        }
        String message = event.getMessage().getContentRaw();
        String prefix = UDatabase.getPrefix(event.getGuild());
        String messageSub = message.substring(prefix.length()).replaceAll(" +", " ");
        String[] arguments = messageSub.split(" ");
        CommandContext context = CommandContext.construct(event.getMessage(), ArrayUtils.remove(arguments, 0));
        if(!InputHandler.INSTANCE.offer(event.getMember(), context)) {
            return;
        }
        Gravity gravity = DataContainer.INSTANCE.getGravity();
        TagData tags = gravity.load(new TagData(event.getGuild().getId()));
        TagActiveData tagActiveData = gravity.load(new TagActiveData(event.getGuild().getId()));
        if (!disableReply) {
            for (String key : tags.getKeys()) {
                if (tagActiveData.contains(key)) {
                    continue;
                }
                Pattern pattern = tagCache.compute(event.getGuild().getIdLong(), key, Pattern.compile(key));
                if (pattern.matcher(message.toLowerCase()).matches()) {
                    event.getChannel().sendMessage(tags.get(key).asString()).queue(null, oops ->
                            event.getChannel().sendMessage("Oh dear. Something went wrong. Ping a dev with this: "
                                    + oops.getMessage())
                                    .queue()
                    );
                    break;
                }
            }
        }
        if(!message.startsWith(prefix)) {
            return;
        }
        String commandName = arguments[0].toLowerCase();
        Command command = IBai.INSTANCE.getCommandRegistry().query(commandName);
        if(command != null) {
            command.processAndExecute(context);
        }
    }

    /**
     * When a message is updated.
     * @param event The event.
     */
    @Override
    public void onGuildMessageUpdate(GuildMessageUpdateEvent event) {
        MinimalMessage message = messageCache.get(event.getMessageIdLong());
        if(message != null) {
            forLogChannel(channel -> {
                MessageEmbed embed = new EmbedBuilder()
                        .setAuthor(author(channel.getJDA(), message) + " edited in #" + event.getChannel().getName())
                        .addField("From", message.getContent(), false)
                        .addField("To", event.getMessage().getContentRaw(), false)
                        .addField("Utilities", String.format(
                                "[21 Jump Street](%s)\nUser: %s",
                                event.getMessage().getJumpUrl(),
                                UFormatter.formatMention(message.getAuthor())), false)
                        .setColor(Color.YELLOW)
                        .build();
                channel.sendMessage(embed).queue();
            }, event);
            message.setContent(event.getMessage().getContentRaw());
        }
    }

    /**
     * When a message is deleted.
     * @param event The event.
     */
    @Override
    public void onGuildMessageDelete(GuildMessageDeleteEvent event) {
        MinimalMessage message = messageCache.get(event.getMessageIdLong());
        if(message == null) {
            return;
        }
        forLogChannel(channel -> {
            MessageEmbed embed = new EmbedBuilder()
                    .setAuthor(author(channel.getJDA(), message) + " deleted in #" + event.getChannel().getName())
                    .setDescription(message.getContent())
                    .addField("Utilities", String.format("User: %s", UFormatter.formatMention(message.getAuthor())), false)
                    .setColor(Color.RED)
                    .build();
            channel.sendMessage(embed).queue();
        }, event);
    }

    /**
     * Handles private messages sent to the bot.
     * @param event The event.
     */
    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        // Presently event is used for users to submit 'odds' guesses.
        String rawMessage = event.getMessage().getContentRaw();
        OddsManager.INSTANCE.newGuess(event.getChannel(), event.getAuthor().getId(), rawMessage);
    }

    /**
     * Gets the logging channel as an object for simplicity's sake.
     * @param consumer The consumer.
     * @param event A generic guild event.
     */
    private void forLogChannel(Consumer<TextChannel> consumer, GenericGuildEvent event) {
        Guild guild = event.getGuild();
        Gravity gravity = DataContainer.INSTANCE.getGravity();
        TextChannel textChannel = guild.getTextChannelById(gravity.load(new GuildData(guild.getId()))
                .get(GuildData.LOGS)
                .defaulting(0L)
                .asLong());
        if(textChannel == null) {
            return;
        }
        consumer.accept(textChannel);
    }

    /**
     * Bot repeats message if it has been repeated 4 times within a channel.
     * @param event Event of when guild message was received.
     */
    private void repeater(GuildMessageReceivedEvent event) {
        String channelID = event.getChannel().getId();
        Long userID = event.getMember().getIdLong();
        String message = event.getMessage().getContentRaw();
        if (recentMessages.containsKey(channelID)) {
            ArrayList<MinimalMessage> messages = recentMessages.get(channelID);
            if (messages.get(0).getContent().equals(message)) {
                if (!messages.stream().map(MinimalMessage::getAuthor).anyMatch(userID::equals)) {
                    messages.add(new MinimalMessage(userID, message));
                    if (messages.size() >= 4) {
                        message.replace("@", "@\u200B");
                        event.getChannel().sendMessage(message).queue();
                        recentMessages.remove(channelID);
                    }
                }
            } else {
                recentMessages.remove(channelID);
            }
        } else {
            ArrayList<MinimalMessage> messages = new ArrayList<>();
            messages.add(new MinimalMessage(userID, message));
            recentMessages.put(channelID, messages);
        }
    }

    /**
     * Method to determine if bot should suppress a reply such as a tag or repeat.
     * Will check if the channel has had replies disabled by command, will override if user is staff.
     * @param event Event of when guild message was received.
     * @return Whether to suppress replies (such as tags and repeats).
     */
    private boolean disableReply(GuildMessageReceivedEvent event) {
        if (CommandPermission.role(GuildData.MODERATOR).hasPermission(event.getMember(), event.getChannel())) {
            return false;
        }

        Gravity gravity = DataContainer.INSTANCE.getGravity();
        String channelID = event.getChannel().getId();
        ReplyData replyData = gravity.load(new ReplyData(event.getGuild().getId()));
        return replyData.contains(channelID);
    }

    /**
     * Method to format the author of a Minimal Message.
     * @param jda JDA
     * @param message Minimal Message
     * @return Formatted String of author.
     */
    private String author(JDA jda, MinimalMessage message) {
        User author = jda.getUserById(message.getAuthor());
        if (author == null) {
            return String.valueOf(message.getAuthor());
        } else {
            return UFormatter.formatUserInfo(author);
        }
    }

}
