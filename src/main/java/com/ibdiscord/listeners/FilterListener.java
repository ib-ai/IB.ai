/* Copyright 2018-2020 Arraying
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
import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.GuildData;
import com.ibdiscord.data.db.entries.filter.FilterData;
import com.ibdiscord.data.db.entries.filter.FilterNotifyData;
import com.ibdiscord.data.db.entries.monitor.MonitorData;
import com.ibdiscord.utils.UDatabase;
import com.ibdiscord.utils.UFormatter;
import com.ibdiscord.utils.objects.GuildedCache;
import de.arraying.gravity.Gravity;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class FilterListener extends ListenerAdapter {

    private final GuildedCache<String, Pattern> filterCache = new GuildedCache<>();

    /**
     * Receives a guild message and checks if it violates the chat filter.
     *
     * @param event The event.
     */
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        handleFilter(event.getMessage());
    }

    /**
     * When a message is updated.
     *
     * @param event The event.
     */
    @Override
    public void onGuildMessageUpdate(GuildMessageUpdateEvent event) {
        handleFilter(event.getMessage());
    }

    /**
     * Handles filtering messages.
     *
     * @param msgObj The JDA message object
     */
    public void handleFilter(Message msgObj) {
        Guild guild = msgObj.getGuild();
        User author = msgObj.getAuthor();

        if (author.getId().equals(guild.getSelfMember().getId())) return;
        GuildChannel parent = guild.getGuildChannelById(msgObj.getChannel().getIdLong()).getParent();
        if (parent != null && IBai.INSTANCE.getConfig().getNsaDenyList().contains(parent.getIdLong())) return;

        String msg = msgObj.getContentRaw();
        String prefix = UDatabase.getPrefix(guild);

        if (msg.startsWith(prefix))
            if (renderCommand(msg, prefix)) return; //Renders command and returns if command name is 'filter'

        Gravity gravity = DataContainer.INSTANCE.getGravity();
        GuildData guildData = gravity.load(new GuildData(guild.getId()));
        FilterData filterData = gravity.load(new FilterData(guild.getId()));
        FilterNotifyData filterNotifyData = gravity.load(new FilterNotifyData(guild.getId()));
        if (!guildData.get(GuildData.FILTERING).defaulting(false).asBoolean()) return;
        Optional<Matcher> match = filterData.values().stream()
                .map(it -> filterCache.compute(guild.getIdLong(),
                        it.asString(),
                        Pattern.compile(it.asString(), Pattern.CASE_INSENSITIVE))
                )
                .map(it -> it.matcher(msg))
                .filter(Matcher::find)
                .findFirst();
        if (!match.isPresent()) return; //Returns if no words match

        msgObj.delete().queue(success -> { //Deletion and warning process of the user/msg
            StringBuilder builder = new StringBuilder(msg).insert(match.get().start(), "**").insert(match.get().end(), "**");
            author.openPrivateChannel().queue(dm -> {
                String send = getWarning(guild.getName(), builder.toString());
                send = send.length() > 2000 ? send.substring(0, 2000) : send;
                dm.sendMessage(send).queue();
            });

            TextChannel monitorChannel = getMonitorChannel(guild, gravity);
            if (monitorChannel == null) {
                IBai.INSTANCE.getLogger().info("Monitor channel not found: %s sent %s", author.getAsTag(), msgObj.getContentRaw());
                return;
            }
            //Log process into the monitor channel.
            String title = UFormatter.formatUserInfo(author);
            if (title.length() > MessageEmbed.TITLE_MAX_LENGTH) title = title.substring(0, MessageEmbed.TITLE_MAX_LENGTH);

            String description = String.format(
                    "\"%s\", sent in **%s** by %s",
                    msgObj.getContentRaw(),
                    msgObj.getTextChannel().getAsMention(),
                    UFormatter.formatMention(author.getId())
            );

            description = description.length() > 2000 ? description.substring(0, 2000) : description;
            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setColor(Color.MAGENTA)
                    .setAuthor("Filter was triggered!")
                    .setTitle(title)
                    .setDescription(description);
            monitorChannel.sendMessageEmbeds(embedBuilder.build()).queue();
            if(!filterNotifyData.contains(match.get().pattern())) monitorChannel.sendMessage("@here").queue();
        });
    }

    /**
     * Renders the message as a command if detect as such.
     *
     * @param msg The message
     * @param prefix Bot prefix
     * **/
    private boolean renderCommand(String msg, String prefix) {
        String messageSub = msg.substring(prefix.length()).replaceAll(" +", " ");
        String[] arguments = messageSub.split(" ");
        String commandName = arguments[0].toLowerCase();
        Command command = IBai.INSTANCE.getCommandRegistry().query(commandName);
        if (command != null && command.getName().equalsIgnoreCase("filter")) return true;
        return false;
    }

    /**
     * Returns the formatted warning message DM's to a user after their use of filtered contents.
     *
     * @param content The content in question.
     * @param guildName Name of the server.
     * **/
    private static String getWarning(String guildName, String content) {
        return String.format("The following message has been flagged and deleted for potentially "
                + "breaking the rules on %s (offending phrase bolded):\n\n%s"
                + "\n\n If you believe you haven't broken any rules, or have any other questions or concerns "
                + "regarding this, you can contact the staff team for clarification by DMing the ModMail bot, "
                + "at the top of the sidebar on the server.", guildName, content);
    }

    //TODO May need a better description of the gravity parameter, as without docs, I have no idea what it does.

    /**
     * Gets the monitor channel.
     *
     * @param guild Guild in question
     * @param gravity Gravity instance
     * **/
    private static TextChannel getMonitorChannel(Guild guild, Gravity gravity){
        MonitorData monitorData = gravity.load(new MonitorData(guild.getId()));
        return guild.getTextChannelById(
                monitorData.get(MonitorData.MESSAGE_CHANNEL).defaulting(0).asLong()
        );
    }
}

