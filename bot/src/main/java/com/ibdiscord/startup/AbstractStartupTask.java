package com.ibdiscord.startup;

import com.ibdiscord.utils.objects.AbstractTask;

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