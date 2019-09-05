package com.ibdiscord.listeners;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.GuildData;
import com.ibdiscord.data.db.entries.TagData;
import com.ibdiscord.input.InputHandler;
import com.ibdiscord.utils.UDatabase;
import com.ibdiscord.utils.objects.ExpiringCache;
import com.ibdiscord.utils.objects.GuildedCache;
import com.ibdiscord.utils.objects.MinimalMessage;
import de.arraying.gravity.Gravity;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.lang3.ArrayUtils;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.regex.Pattern;

/**
 * Copyright 2017-2019 Jarred Vardy, Arraying
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
public final class MessageListener extends ListenerAdapter {

    private final GuildedCache<String, Pattern> tagCache = new GuildedCache<>();
    private final ExpiringCache<Long, MinimalMessage> messageCache = new ExpiringCache<>(1, TimeUnit.HOURS);

    /**
     * When an message is sent in a guild channel (because DMs are boring).
     * @param event The event instance.
     */
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        messageCache.put(event.getMessageIdLong(), new MinimalMessage(event.getAuthor().getIdLong(), event.getMessage().getContentRaw()));
        if(event.getAuthor().isBot()
                || event.getAuthor().isFake()
                || !event.getGuild().getSelfMember().hasPermission(event.getChannel(), Permission.MESSAGE_WRITE)) {
            return;
        }
        String message = event.getMessage().getContentRaw();
        if(!InputHandler.INSTANCE.offer(event.getMember(), event.getMessage())) {
            return;
        }
        Gravity gravity = DataContainer.INSTANCE.getGravity();
        TagData tags = gravity.load(new TagData(event.getGuild().getId()));
        for(String key : tags.getKeys()) {
            Pattern pattern = tagCache.compute(event.getGuild().getIdLong(), key, Pattern.compile(key));
            if(pattern.matcher(message.toLowerCase()).matches()) {
                event.getChannel().sendMessage(tags.get(key).asString()).queue(null, oops ->
                        event.getChannel().sendMessage("Oh dear. Something went wrong. Ping a dev with this: " + oops.getMessage()).queue()
                );
                break;
            }
        }
        String prefix = UDatabase.getPrefix(event.getGuild());
        if(!message.startsWith(prefix)) {
            return;
        }
        message = message.substring(prefix.length()).replaceAll(" +", " ");
        String[] arguments = message.split(" ");
        String commandName = arguments[0].toLowerCase();
        Command command = Command.find(null, commandName);
        if(command != null) {
            command.preprocess(CommandContext.construct(event.getMessage(), ArrayUtils.remove(arguments, 0)));
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
                User author = channel.getJDA().getUserById(message.getAuthor());
                MessageEmbed embed = new EmbedBuilder()
                        .setAuthor((author == null ? String.valueOf(message.getAuthor()) : author.getAsTag()) + " edited in #" + event.getChannel().getName())
                        .addField("From", message.getContent(), false)
                        .addField("To", event.getMessage().getContentRaw(), false)
                        .addField("\u200B", "[Click me to jump](" + event.getMessage().getJumpUrl() + ")", false)
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
            User author = channel.getJDA().getUserById(message.getAuthor());
            MessageEmbed embed = new EmbedBuilder()
                    .setAuthor((author == null ? String.valueOf(message.getAuthor()) : author.getAsTag()) + " deleted in #" + event.getChannel().getName())
                    .setDescription(message.getContent())
                    .build();
            channel.sendMessage(embed).queue();
        }, event);
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

}
