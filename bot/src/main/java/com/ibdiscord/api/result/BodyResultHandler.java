package com.ibdiscord.api.result;

import de.arraying.kotys.JSON;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.util.function.BiConsumer;

/**
 * Copyright 2019 Arraying
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
