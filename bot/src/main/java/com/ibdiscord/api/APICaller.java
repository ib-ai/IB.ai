package com.ibdiscord.api;

import com.ibdiscord.IBai;
import com.ibdiscord.api.result.ResultHandler;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

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
        String base = IBai.INSTANCE.getConfig().getApiBase();
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
