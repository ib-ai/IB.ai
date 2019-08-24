package com.ibdiscord.startup.tasks;

import com.ibdiscord.data.db.DataContainer;
import com.ibdiscord.startup.AbstractStartupTask;

/**
 * Copyright 2017-2019 Jarred Vardy
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
public final class StartDatabase extends AbstractStartupTask {

    public StartDatabase() {
        super("Start-Database");
    }

    /**
     * Initializes/starts up the Redis database.
     */
    @Override
    public void doTask() {
        DataContainer.INSTANCE.connect();
    }

}
