/*******************************************************************************
 * Copyright 2018 pants
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.ibdiscord.formatting;

import com.ibdiscord.utils.objects.Tuple;
import net.dv8tion.jda.core.EmbedBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/**
 * @author pants
 * @since 2018.09.19
 */

public class EmbedFileParser {

    private ArrayList<String> metaFields = new ArrayList<>();
    private ArrayList<String> descriptionFields = new ArrayList<>();

    private String title;
    private boolean isInline;
    private ArrayList<String> fieldContent = new ArrayList<>();
    //  Arraylist of tuples containing:    ArrayList of contents of field's body
    //                                     Tuple representing title of field containing:    Title content
    //                                                                                      Inline?
    //
    private ArrayList<Tuple<Tuple<String, Boolean>, ArrayList<String>>> fields = new ArrayList<>();

    public EmbedBuilder parse(String filePath, Embedder embedder) {
        EmbedBuilder builder = new EmbedBuilder();
        File embedFile;

        try {
            embedFile = new File(filePath);
            BufferedReader reader = new BufferedReader(new FileReader(embedFile));

            // Iterating through each line of the file
            String line = reader.readLine();
            while (line != null) {
                interpretLine(line, builder, embedder);
                line = reader.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return builder;
    }

    private void interpretLine(String line, EmbedBuilder builder, Embedder embedder) {
        String linePrefix = line.substring(0, 2);
        String lineRemainder = line.replace(linePrefix, "");
        LineType type;

        if((linePrefix.equals(">>") || linePrefix.equals("||") ||
            linePrefix.equals("##") || linePrefix.equals("#"))
                && !fieldContent.isEmpty()) {

            StringBuilder content = new StringBuilder();
            for(String str : fieldContent) {
                content.append(str + "\n");
            }

            builder.addField(title, content.toString(), isInline);
        }

        switch(linePrefix) {

            case ">>": metaFields.add(lineRemainder);
                       break;

            case "||": descriptionFields.add(lineRemainder);
                       break;

            case "##": isInline = true;
                       title = lineRemainder;
                       break;

            case "#": title = lineRemainder;
                      break;

            default: fieldContent.add(line);
                break;
        }
    }
}
