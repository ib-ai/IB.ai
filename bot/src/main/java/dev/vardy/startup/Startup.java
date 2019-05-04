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

package dev.vardy.startup;

import dev.vardy.startup.tasks.StartBot;
import dev.vardy.startup.tasks.StartCommands;
import dev.vardy.startup.tasks.StartDatabase;
import dev.vardy.utils.objects.AbstractTask;

import java.util.Arrays;

public final class Startup {

    /**
     * All startup tasks.
     */
    private static final AbstractStartupTask[] STARTUP_TASKS = new AbstractStartupTask[] {
            new StartDatabase(),
            new StartCommands(),
    };

    /**
     * Starts all startup tasks.
     */
    public static void start() {
        for(AbstractStartupTask task : STARTUP_TASKS) {
            task.create();
        }

        new AbstractTask("Task-Startup-Waiting") {

            @Override
            public void execute() {
                while (!Arrays.stream(STARTUP_TASKS).allMatch(AbstractStartupTask::isCompleted)) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ignored) {}
                }
                new StartBot().create();
            }

        }.create();
    }

}
