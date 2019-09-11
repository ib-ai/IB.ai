/* Copyright 2017-2019 Arraying
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

package com.ibdiscord.reminder;

import com.ibdiscord.IBai;
import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.data.db.entries.reminder.ReminderData;
import com.ibdiscord.data.db.entries.reminder.ReminderUserData;
import com.ibdiscord.data.db.entries.reminder.ReminderUserList;
import de.arraying.gravity.Gravity;
import de.arraying.gravity.data.property.Property;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public enum ReminderHandler {

    /**
     * The singleton instance.
     */
    INSTANCE;

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    /**
     * Gets all reminders for the user.
     * @param user The user.
     * @return A list of reminders, can be empty.
     */
    public List<Reminder> getFor(User user) {
        Gravity gravity = DataContainer.INSTANCE.getGravity();
        ReminderUserList userList = gravity.load(new ReminderUserList(user.getId()));
        List<Reminder> list = new ArrayList<>();
        userList.values().stream()
                .map(Property::asInt)
                .forEach(it -> {
                    ReminderUserData reminderUserData = gravity.load(new ReminderUserData(it));
                    if(reminderUserData.get(ReminderUserData.USER)
                            .defaulting(0L)
                            .asLong() == user.getIdLong()) {
                        Reminder reminder = new Reminder(it,
                                reminderUserData.get(ReminderUserData.TIME)
                                        .defaulting(0L)
                                        .asLong(),
                                reminderUserData.get(ReminderUserData.TEXT)
                                        .defaulting("oops, an error occurred")
                                        .asString());
                        list.add(reminder);
                    }
                });
        return list;
    }

    /**
     * Creates a reminder for a user.
     * @param user The user.
     * @param time The time.
     * @param text The text.
     */
    public synchronized void create(User user, long time, String text) {
        Gravity gravity = DataContainer.INSTANCE.getGravity();
        ReminderData reminderData = gravity.load(new ReminderData());
        int newId = reminderData.get()
                .defaulting(0)
                .asInt() + 1;
        reminderData.set(newId);
        gravity.save(reminderData);
        ReminderUserData reminderUserData = gravity.load(new ReminderUserData(newId));
        reminderUserData.set(ReminderUserData.TEXT, text);
        reminderUserData.set(ReminderUserData.TIME, time);
        reminderUserData.set(ReminderUserData.USER, user.getId());
        gravity.save(reminderUserData);
        ReminderUserList reminderUserList = gravity.load(new ReminderUserList(user.getId()));
        reminderUserList.add(newId);
        gravity.save(reminderUserList);
        Reminder reminder = new Reminder(newId, time, text);
        schedule(reminder, user);
    }

    /**
     * Schedules a reminder.
     * @param reminder The reminder.
     * @param user The user.
     */
    public void schedule(Reminder reminder, User user) {
        if(reminder.isCompleted()) {
            return;
        }
        long now = System.currentTimeMillis();
        long end = reminder.getTime();
        long diff = end - now;
        long schedule = diff > 0 ? diff : 0;
        executorService.schedule(() -> {
            if(reminder.isCompleted()) {
                return;
            }
            user.openPrivateChannel().queue(channel -> channel.sendMessage("Reminder! You asked me to remind "
                        + "you of: " + reminder.getReminder())
                    .queue());
            reminder.setCompleted(true);
        }, schedule, TimeUnit.MILLISECONDS);
        IBai.INSTANCE.getLogger().info("Scheduled reminder #{}.", reminder.getId());
    }

}
