/* Copyright 2018-2020 Jarred Vardy
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

package com.ibdiscord.utils.objects;

public abstract class AbstractTask implements Runnable {

    private final String name;

    /**
     * Creates a task.
     * @param name The name of the task.
     */
    public AbstractTask(String name) {
        this.name = name;
    }

    /**
     * Abstract execution.
     */
    protected abstract void execute();

    /**
     * Creates the startup task.
     * This creates the thread, sets the name and starts it.
     */
    public void create() {
        Thread currentThread = new Thread(this);
        currentThread.setName(name);
        currentThread.start();
    }

    /**
     * The actual execution.
     */
    @Override
    public void run() {
        execute();
    }

}
