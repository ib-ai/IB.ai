/**
 * Copyright 2017-2019 Jarred Vardy
 *
 * This file is part of LoyalBot.
 *
 * LoyalBot is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LoyalBot is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LoyalBot. If not, see http://www.gnu.org/licenses/.
 */

package dev.vardy.startup.tasks;

import dev.vardy.data.db.DContainer;
import dev.vardy.startup.AbstractStartupTask;

public final class StartDatabase extends AbstractStartupTask {

    public StartDatabase() {
        super("Start-Database");
    }

    /**
     * Initializes/starts up the Redis database.
     */
    @Override
    public void doTask() {
        DContainer.INSTANCE.connect();
    }

}
