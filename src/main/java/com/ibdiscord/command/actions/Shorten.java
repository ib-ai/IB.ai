/* Copyright 2017-2020 Jarred Vardy <vardy@riseup.net>
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

package com.ibdiscord.command.actions;

import com.ibdiscord.IBai;
import com.ibdiscord.command.CommandAction;
import com.ibdiscord.command.CommandContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Shorten implements CommandAction {

    /**
     * Executes the command.
     * @param context The command context.
     */
    @Override
    public void accept(CommandContext context) {
        context.assertArguments(1, "error.generic_arg_length");

        StringBuilder response = new StringBuilder();
        try {
            //URL url = new URL(IBai.INSTANCE.getConfig().getShortUrl());
            URL url = new URL("hhttps://ibpp.me/function/shorten");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            try(OutputStream os = con.getOutputStream()) {
                String urlToShorten = context.getArguments()[0];
                String payload = String.format("data={\"page\":%s}", urlToShorten);

                byte[] input = payload.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"))) {
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }
        } catch (IOException ex) {
            context.replyRaw("Something went wrong with the POST request. Report to Pants.");
            context.replyRaw(ex.toString());
        }

        if(response.toString() != "") {
            context.replyRaw(String.format("%s", response.toString()));
        }
    }
}
