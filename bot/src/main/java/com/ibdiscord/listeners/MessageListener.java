package com.ibdiscord.listeners;

import com.ibdiscord.IBai;
import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.data.db.DContainer;
import com.ibdiscord.data.db.entries.GuildData;
import com.ibdiscord.data.db.entries.TagData;
import com.ibdiscord.utils.UDatabase;
import com.ibdiscord.utils.objects.ExpiringCache;
import com.ibdiscord.utils.objects.GuildedCache;
import com.ibdiscord.utils.objects.MinimalMessage;
import de.arraying.gravity.Gravity;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.apache.commons.lang3.ArrayUtils;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

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
        Gravity gravity = DContainer.INSTANCE.getGravity();
        TagData tags = gravity.load(new TagData(event.getGuild().getId()));
        for(String key : tags.getKeys()) {
            try {
                Pattern pattern = tagCache.compute(event.getGuild().getIdLong(), key, Pattern.compile(key));
                if(pattern.matcher(message.toLowerCase()).matches()) {
                    event.getChannel().sendMessage(tags.get(key).asString()).queue(null, oops ->
                            event.getChannel().sendMessage("Oh dear. Something went wrong. Ping a dev with this: " + oops.getMessage()).queue()
                    );
                    break;
                }
            } catch(PatternSyntaxException exception) {
                IBai.INSTANCE.getLogger().info("Tag \"{}\" failed in guild {}: {}", key, event.getGuild().getId(), exception.getMessage());
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
        Guild guild = event.getGuild();
        Gravity gravity = DContainer.INSTANCE.getGravity();
        TextChannel textChannel = guild.getTextChannelById(gravity.load(new GuildData(guild.getId()))
                .get(GuildData.LOGS)
                .defaulting(0L)
                .asLong());
        if(textChannel == null) {
            return;
        }
        User author = guild.getJDA().getUserById(message.getAuthor());
        MessageEmbed embed = new EmbedBuilder()
                .setAuthor(author == null ? String.valueOf(message.getAuthor()) : author.getAsTag())
                .setDescription(message.getContent())
                .build();
        textChannel.sendMessage(embed).queue();
    }

}
