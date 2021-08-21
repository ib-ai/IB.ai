/* Copyright 2017-2020 Arraying
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

package com.ibdiscord.i18n;

import com.ibdiscord.command.CommandContext;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.time.temporal.TemporalAccessor;
import java.util.List;

/**
 * Please refer to {@link net.dv8tion.jda.api.EmbedBuilder} for documentation.
 * The methods here are just wrappers.
 * The reason that this doesn't simply extend EmbedBuilder and is a wrapper is because of a final method.
 * Unfortunately, the one method that is final is one that's useful, so this abomination needs to be written.
 * Apparently there are reasons for this being final, though.
 * Additionally, all the formatter variables are slightly annoying so instead of method overloading we can use
 * custom objects instead.
 */
public final class EmbedBuilderI18n {

    private final EmbedBuilder nest = new EmbedBuilder();
    private final CommandContext context;

    /**
     * Creates a new internationalization compatible embed builder.
     * @param context The command context.
     */
    public EmbedBuilderI18n(CommandContext context) {
        this.context = context;
    }

    /**
     * See parent.
     * @param inline See parent.
     * @return See parent.
     */
    public EmbedBuilderI18n addBlankField(boolean inline) {
        nest.addBlankField(inline);
        return this;
    }

    /**
     * See parent.
     * @param name See parent.
     * @param value See parent.
     * @param inline See parent.
     * @return See parent.
     */
    public EmbedBuilderI18n addField(StringI18n name, StringI18n value, boolean inline) {
        nest.addField(i18n(name), i18n(value), inline);
        return this;
    }

    /**
     * See parent.
     * @param field See parent.
     * @return See parent.
     */
    public EmbedBuilderI18n addField(MessageEmbed.Field field) {
        nest.addField(field);
        return this;
    }

    /**
     * See parent.
     * @param sequence See parent.
     * @return See parent.
     */
    public EmbedBuilderI18n appendDescription(StringI18n sequence) {
        nest.appendDescription(i18n(sequence));
        return this;
    }

    /**
     * See parent.
     * @return See parent.
     */
    public MessageEmbed build() {
        return nest.build();
    }

    /**
     * See parent.
     * @return See parent.
     */
    public EmbedBuilderI18n clear() {
        nest.clear();
        return this;
    }

    /**
     * See parent.
     * @return See parent.
     */
    public EmbedBuilderI18n clearFields() {
        nest.clearFields();
        return this;
    }

    /**
     * See parent.
     * @return See parent.
     */
    public StringBuilder getDescriptionBuilder() {
        return nest.getDescriptionBuilder();
    }

    /**
     * See parent.
     * @return See parent.
     */
    public List<MessageEmbed.Field> getFields() {
        return nest.getFields();
    }

    /**
     * See parent.
     * @return See parent.
     */
    public boolean isEmpty() {
        return nest.isEmpty();
    }

    /**
     * See parent.
     * @return See parent.
     */
    public boolean isValidLength() {
        return nest.isValidLength();
    }

    /**
     * See parent.
     * @return See parent.
     */
    public int length() {
        return nest.length();
    }

    /**
     * See parent.
     * @param name See parent.
     * @return See parent.
     */
    public EmbedBuilderI18n setAuthor(StringI18n name) {
        nest.setAuthor(i18n(name));
        return this;
    }

    /**
     * See parent.
     * @param name See parent.
     * @param url See parent.
     * @return See parent.
     */
    public EmbedBuilderI18n setAuthor(StringI18n name, String url) {
        nest.setAuthor(i18n(name), url);
        return this;
    }

    /**
     * See parent.
     * @param name See parent.
     * @param url See parent.
     * @param iconUrl See parent.
     * @return See parent.
     */
    public EmbedBuilderI18n setAuthor(StringI18n name, String url, String iconUrl) {
        nest.setAuthor(i18n(name), url, iconUrl);
        return this;
    }

    /**
     * See parent.
     * @param color See parent.
     * @return See parent.
     */
    public EmbedBuilderI18n setColor(int color) { // If this wasn't 1:1 I would rename to setColour. Frick US spelling.
        nest.setColor(color);
        return this;
    }

    /**
     * See parent.
     * @param color See parent.
     * @return See parent.
     */
    public EmbedBuilderI18n setColor(Color color) {
        nest.setColor(color);
        return this;
    }

    /**
     * See parent.
     * @param description See parent.
     * @return See parent.
     */
    public EmbedBuilderI18n setDescription(StringI18n description) {
        nest.setDescription(i18n(description));
        return this;
    }

    /**
     * See parent.
     * @param text See parent.
     * @return See parent.
     */
    public EmbedBuilderI18n setFooter(StringI18n text) {
        nest.setFooter(i18n(text));
        return this;
    }

    /**
     * See parent.
     * @param text See parent.
     * @param iconUrl See parent.
     * @return See parent.
     */
    public EmbedBuilderI18n setFooter(StringI18n text, String iconUrl) {
        nest.setFooter(i18n(text), iconUrl);
        return this;
    }

    /**
     * See parent.
     * @param url See parent.
     * @return See parent.
     */
    public EmbedBuilderI18n setImage(String url) {
        nest.setImage(url);
        return this;
    }

    /**
     * See parent.
     * @param url See parent.
     * @return See parent.
     */
    public EmbedBuilderI18n setThumbnail(String url) {
        nest.setThumbnail(url);
        return this;
    }

    /**
     * See parent.
     * @param temporal See parent.
     * @return See parent.
     */
    public EmbedBuilderI18n setTimestamp(TemporalAccessor temporal) {
        nest.setTimestamp(temporal);
        return this;
    }

    /**
     * See parent.
     * @param title See parent.
     * @return See parent.
     */
    public EmbedBuilderI18n setTitle(StringI18n title) {
        nest.setTitle(i18n(title));
        return this;
    }

    /**
     * See parent.
     * @param title See parent.
     * @param url See parent.
     * @return See parent.
     */
    public EmbedBuilderI18n setTitle(StringI18n title, String url) {
        nest.setTitle(i18n(title), url);
        return this;
    }

    /**
     * Wrapper method.
     * @param stringI18n The localized string.
     * @return A string.
     */
    private String i18n(StringI18n stringI18n) {
        return stringI18n.parse(context);
    }

}
