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
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;

public class Shorten implements CommandAction {

    /**
     * Executes the command.
     * @param context The command context.
     */
    @Override
    public void accept(CommandContext context) {
        context.assertArguments(1, "error.generic_arg_length");

        String url = context.getArguments()[0];
        String payload = String.format("data={\"page\":%s}", url);
        StringEntity entity = new StringEntity(payload,
                ContentType.APPLICATION_FORM_URLENCODED);

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost("https://ibpp.me/function/shorten");
        //HttpPost request = new HttpPost(IBai.INSTANCE.getConfig().getShortUrl());
        request.setEntity(entity);

        HttpResponse response = null;
        try {
            response = httpClient.execute(request);
        } catch (IOException ex) {
            context.replyRaw("Something went wrong with the POST request. Report to Pants.");
        }

        if(response != null) {
            context.replyRaw(String.format("<%s>", response));
        } else {
            context.replyRaw("Something went wrong with the POST request. Report to Pants.");
        }
    }
}
