/* Copyright 2017-2020 Arraying
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

package com.ibdiscord.command.registrar;

import com.ibdiscord.command.registry.CommandRegistrar;
import com.ibdiscord.command.registry.CommandRegistry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public final class RegistrarFun implements CommandRegistrar {

    /**
     * Registers commands.
     * @param registry The command registry.
     */
    @Override
    public void register(CommandRegistry registry) {
        registry.define("coinflip")
                .on(context -> context.replyI18n(new Random().nextInt(2) == 0
                        ? "success.coinflip_heads"
                        : "success.coinflip_tails"
                ));

        registry.define("dadjoke")
                .on(context -> {
                    try {

                        URL url = new URL("https://icanhazdadjoke.com/");
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestProperty("Accept", "text/plain");
                        connection.setRequestMethod("GET");

                        try(BufferedReader in = new BufferedReader(
                                new InputStreamReader(connection.getInputStream()))) {

                            StringBuilder jokeBuilder = new StringBuilder();

                            String line;
                            while((line = in.readLine()) != null) {
                                jokeBuilder.append(line);
                                jokeBuilder.append(System.lineSeparator());
                            }

                            String response = jokeBuilder.toString();
                            context.replyRaw(response);
                        }

                    } catch(IOException exception) {
                        exception.printStackTrace();
                        context.replyI18n("error.generic");
                    }
                });
    }

}
