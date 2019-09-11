/* Copyright 2017-2019 Arraying
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

package com.ibdiscord.api.result;

import de.arraying.kotys.JSON;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.util.function.BiConsumer;

public final class BodyResultHandler implements ResultHandler {

    private final BiConsumer<Integer, JSON> handler;

    /**
     * Creates the new body result handler.
     * @param handler The handler.
     */
    public BodyResultHandler(BiConsumer<Integer, JSON> handler) {
        this.handler = handler;
    }

    /**
     * Turns the body into JSON.
     * @param response The response.
     */
    @Override
    public void handle(Response response) {
        JSON json = null;
        ResponseBody responseBody = response.body();
        if(responseBody != null) {
            try {
                json = new JSON(responseBody.string());
            } catch(IOException exception) {
                exception.printStackTrace();
            }
            responseBody.close();
        }
        if(json == null) {
            json = new JSON();
        }
        handler.accept(response.code(), json);
    }

}
