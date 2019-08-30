package com.ibdiscord.startup.tasks;

import com.ibdiscord.localisation.Localiser;
import com.ibdiscord.startup.AbstractStartupTask;

/**
 * Copyright 2017-2019 Jarred Vardy <jarred.vardy@gmail.com>
 * <p>
 * This file is part of IB.ai.
 * <p>
 * IB.ai is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * IB.ai is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with IB.ai. If not, see http://www.gnu.org/licenses/.
 */
public class StartLocaliser extends AbstractStartupTask {


    /**
     * Creates the startup task.
     */
    public StartLocaliser() {
        super("Start-Localiser");
    }

    @Override
    protected void doTask() {
        Localiser.INSTANCE.init();
    }
}
