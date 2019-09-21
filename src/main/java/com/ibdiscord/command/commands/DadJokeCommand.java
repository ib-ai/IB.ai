/* Copyright 2017-2019 Jarred Vardy
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

package com.ibdiscord.command.commands;

import com.ibdiscord.command.Command;
import com.ibdiscord.command.CommandContext;
import com.ibdiscord.command.permissions.CommandPermission;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Set;

public final class DadJokeCommand extends Command {

    /**
     * Creates a new dad joke command.
     */
    public DadJokeCommand() {
        super("dadjoke",
                CommandPermission.discord(),
                Set.of()
        );
    }

    @Override
    protected void execute(CommandContext context) {
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
                context.reply(response);
            }

        } catch(IOException ex) {
            context.reply("Something went wrong...");
            ex.printStackTrace();
        }
    }
}
