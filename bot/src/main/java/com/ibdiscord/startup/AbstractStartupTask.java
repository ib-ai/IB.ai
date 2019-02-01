package com.ibdiscord.startup;

import com.ibdiscord.utils.objects.AbstractTask;

/**
 * Copyright 2019 Jarred Vardy
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
public abstract class AbstractStartupTask extends AbstractTask {

    private boolean completed = false;

    /**
     * Creates an abstract startup task.
     * @param name The name of the task.
     */
    protected AbstractStartupTask(String name) {
        super(name);
    }

    /**
     * The method that is invoked when the task is executed.
     * @throws Exception Any exception that occurred during startup.
     */
    protected abstract void doTask() throws Exception;

    /**
     * Executes the task.
     */
    @Override
    public void execute() {
        try {
            doTask();
            completed = true;

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Whether or not the task has completed.
     * @return True if it has, false otherwise.
     */
    boolean isCompleted() {
        return completed;
    }

}