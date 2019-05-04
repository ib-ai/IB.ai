/**
 * Copyright 2017-2019 Arraying
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

package dev.vardy.api;

import dev.vardy.LoyalBot;
import dev.vardy.api.result.ResultHandler;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public enum APICaller {

    /**
     * The singleton instance.
     */
    INSTANCE;

    private final OkHttpClient okHttpClient = new OkHttpClient();

    /**
     * Dispatches an API request.
     * This needs to be expanded to allow request bodies.
     * @param route The route.
     * @param resultHandler The result handler.
     * @return True if the request succeeded, false otherwise.
     */
    public boolean dispatch(Route route, ResultHandler resultHandler) {
        String base = LoyalBot.INSTANCE.getConfig().getApiBase();
        Request request = new Request.Builder()
                .url(base + route.getPath())
                .method(route.getMethod(), null)
                .build();
        try(Response response = okHttpClient.newCall(request).execute()) {
            resultHandler.handle(response);
            return true;
        } catch(IOException exception) {
            exception.printStackTrace();
            return false;
        }
    }

}
