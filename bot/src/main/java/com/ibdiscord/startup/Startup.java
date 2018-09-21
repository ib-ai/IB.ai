/*******************************************************************************
 * Copyright 2018 pants
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.ibdiscord.startup;

import com.ibdiscord.startup.tasks.StartBot;
import com.ibdiscord.startup.tasks.StartCommands;
import com.ibdiscord.startup.tasks.StartDatabase;
import com.ibdiscord.utils.objects.AbstractTask;

import java.util.Arrays;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/**
 * @author pants
 * @since 2018.08.22
 */

public final class Startup {

    private static final AbstractStartupTask[] startupTasks = new AbstractStartupTask[] {
            new StartDatabase(),
            new StartCommands(),
    };

    public static void start() {
        for(AbstractStartupTask task : startupTasks) {
            task.create();
        }

        new AbstractTask("Task-Startup-Waiting") {

            @Override
            public void execute() {
                while (!Arrays.stream(startupTasks).allMatch(AbstractStartupTask::isCompleted)) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ignored) {}
                }
                new StartBot().create();
            }

        }.create();
    }
}
