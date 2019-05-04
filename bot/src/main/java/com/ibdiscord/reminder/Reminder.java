package com.ibdiscord.reminder;

import com.ibdiscord.data.db.DContainer;
import com.ibdiscord.data.db.entries.reminder.ReminderUserData;
import de.arraying.gravity.Gravity;
import lombok.AllArgsConstructor;

import java.util.Date;

/**
 * Copyright 2017-2019 Arraying
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
