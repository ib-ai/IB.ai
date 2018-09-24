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

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/**
 * @author pants
 * @since 2018.09.19
 */

public class Embedder {

    private static final String EMBEDS_LOCATION = "./content/embeds/";

    // Embedder-specific fields
    private EmbedStyle style;
    private EmbedType type;
    private boolean multiPaged;

    // Discord Embed fields
    private String title;
    private String title_url;

    // Variables to serve
    private EmbedBuilder builderFinal;
    private MessageEmbed embedFinal;

    public Embedder(String fileName) {
        parse(EMBEDS_LOCATION + fileName, this);
    }

    private void parse(String fileLocation, Embedder embedder) {
        EmbedFileParser parser = new EmbedFileParser();
        builderFinal = parser.parse(fileLocation, embedder);
        embedFinal = builderFinal.build();
    }
}
