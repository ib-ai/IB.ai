/* Copyright 2017-2021 Arraying
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

package com.ibdiscord.ibai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Bootstrap {

    /**
     * Runs everything under Spring Boot.
     * @param args The runtime arguments.
     */
    public static void main(String[] args) {
        // Load disabled modules from environment variable.
        String disabledModules = System.getenv("DISABLED_MODULES");
        if (disabledModules != null && !disabledModules.isEmpty()) {
            String[] modules = disabledModules.split(",");
            for (String module : modules) {
                System.setProperty("module." + module, "false");
            }
        }
        SpringApplication.run(Bootstrap.class, args);
    }
}
