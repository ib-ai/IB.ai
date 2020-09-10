/* Copyright 2018-2020 Jarred Vardy <vardy@riseup.net>
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

package com.ibdiscord.utils;

import com.ibdiscord.command.CommandContext;
import de.arraying.kotys.JSON;
import de.arraying.kotys.JSONArray;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.*;
import java.net.URL;

public final class UJSON {

    /**
     * Reads a file from disk and loads data into a JSON object.
     * @param relativeFilePath Relative path to JSON file from project root
     * @param charset The charset to use to decode the file.
     * @return JSON object corresponding to file at desired location.
     * @throws IOException When there is an I/O error.
     */
    public static JSON retrieveJSONFromFile(String relativeFilePath, String charset)
            throws IOException {
        StringBuilder jsonBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(new File(relativeFilePath)),
                charset
        ));
        String line = reader.readLine();
        while(line != null) {
            jsonBuilder.append(line);
            line = reader.readLine();
        }
        reader.close();
        return new JSON(jsonBuilder.toString());
    }

    /**
     * Alias to {@link #retrieveJSONFromFile(String, String)} with default UTF-8 encoding.
     * @param relativeFilePath Relative path to JSON file from project root.
     * @return JSON object corresponding to file at desired location.
     * @throws IOException When there is an I/O error.
     */
    public static JSON retrieveJSONFromFile(String relativeFilePath)
            throws IOException {
        return retrieveJSONFromFile(relativeFilePath, "UTF-8");
    }

    /**
     * Extracts array from given URL.
     * @param context The command context.
     * @param url Url containing JSON.
     * @return JSONArray containing array from URL.
     * @throws IOException When there is an I/O error.
     */
    public static JSONArray retrieveJSONArrayFromURL(CommandContext context, URL url)
        throws IOException {
        OkHttpClient http = context.getJda().getHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = http.newCall(request).execute();
        return new JSONArray(response.body().string());
    }

}
