package com.ibdiscord.reminder;

import com.ibdiscord.data.db.DContainer;
import com.ibdiscord.data.db.entries.reminder.ReminderUserData;
import de.arraying.gravity.Gravity;
import lombok.AllArgsConstructor;

import java.util.Date;

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
public @AllArgsConstructor final class Reminder {

    private final int id;
    private final long time;
    private final String reminder;

    /**
     * Gets the ID.
     * @return The ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Whether or not the reminder is completed.
     * @return True if it is, false otherwise.
     */
    public boolean isCompleted() {
        return DContainer.INSTANCE.getGravity().load(new ReminderUserData(id)).get(ReminderUserData.COMPLETED)
                .defaulting(false)
                .asBoolean();
    }

    /**
     * Sets the reminder's completed value.
     * @param completed True if it is, false otherwise.
     */
    public void setCompleted(boolean completed) {
        Gravity gravity = DContainer.INSTANCE.getGravity();
        ReminderUserData userData = gravity.load(new ReminderUserData(id));
        userData.set(ReminderUserData.COMPLETED, completed);
        gravity.save(userData);
    }

    /**
     * Gets the time.
     * @return The time in milliseconds.
     */
    public long getTime() {
        return time;
    }

    /**
     * Gets the date.
     * @return The date.
     */
    public Date getDate() {
        return new Date(time);
    }

    /**
     * Gets the actual reminder.
     * @return The reminder.
     */
    public String getReminder() {
        return reminder;
    }

}
