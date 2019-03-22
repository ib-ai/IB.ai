package com.ibdiscord.reminder;

import com.ibdiscord.IBai;
import com.ibdiscord.data.db.DContainer;
import com.ibdiscord.data.db.entries.reminder.ReminderData;
import com.ibdiscord.data.db.entries.reminder.ReminderUserData;
import com.ibdiscord.data.db.entries.reminder.ReminderUserList;
import de.arraying.gravity.Gravity;
import de.arraying.gravity.data.property.Property;
import net.dv8tion.jda.core.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Copyright 2019 Arraying
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
        Gravity gravity = DContainer.INSTANCE.getGravity();
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
     * @return The reminder object.
     */
    public synchronized Reminder create(User user, long time, String text) {
        Gravity gravity = DContainer.INSTANCE.getGravity();
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
        return reminder;
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
            user.openPrivateChannel().queue(channel -> channel.sendMessage("Reminder! You asked me to remind you of: " + reminder.getReminder()).queue());
            reminder.setCompleted(true);
        }, schedule, TimeUnit.MILLISECONDS);
        IBai.INSTANCE.getLogger().info("Scheduled reminder #{}.", reminder.getId());
    }

}
