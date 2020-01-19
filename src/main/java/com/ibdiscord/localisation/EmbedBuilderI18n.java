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
package com.ibdiscord.localisation;

import com.ibdiscord.command.CommandContext;
import net.dv8tion.jda.api.AccountType;
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
public final class EmbedBuilderI18n implements ILocalised {

    private final EmbedBuilder nest = new EmbedBuilder();
    private final CommandContext context;

    /**
     * Creates a new internationalization compatible embed builder.
     * @param context The command context.
     */
    public EmbedBuilderI18n(CommandContext context) {
        this.context = context;
    }

    public EmbedBuilderI18n addBlankField(boolean inline) {
        nest.addBlankField(inline);
        return this;
    }

    public EmbedBuilderI18n addField(StringI18n name, StringI18n value, boolean inline) {
        nest.addField(i18n(name), i18n(value), inline);
        return this;
    }

    public EmbedBuilderI18n addField(MessageEmbed.Field field) {
        nest.addField(field);
        return this;
    }

    public EmbedBuilderI18n appendDescription(StringI18n sequence) {
        nest.appendDescription(i18n(sequence));
        return this;
    }

    public MessageEmbed build() {
        return nest.build();
    }

    public EmbedBuilderI18n clear() {
        nest.clear();
        return this;
    }

    public EmbedBuilderI18n clearFields() {
        nest.clearFields();
        return this;
    }

    public StringBuilder getDescriptionBuilder() {
        return nest.getDescriptionBuilder();
    }

    public List<MessageEmbed.Field> getFields() {
        return nest.getFields();
    }

    public boolean isEmpty() {
        return nest.isEmpty();
    }

    public boolean isValidLength(AccountType type) {
        return nest.isValidLength(type);
    }

    public int length() {
        return nest.length();
    }

    public EmbedBuilderI18n setAuthor(StringI18n name) {
        nest.setAuthor(i18n(name));
        return this;
    }

    public EmbedBuilderI18n setAuthor(StringI18n name, String url) {
        nest.setAuthor(i18n(name), url);
        return this;
    }

    public EmbedBuilderI18n setAuthor(StringI18n name, String url, String iconUrl) {
        nest.setAuthor(i18n(name), url, iconUrl);
        return this;
    }

    public EmbedBuilderI18n setColor(int color) { // If this wasn't 1:1 I would rename to setColour. Frick US spelling.
        nest.setColor(color);
        return this;
    }

    public EmbedBuilderI18n setColor(Color color) {
        nest.setColor(color);
        return this;
    }

    public EmbedBuilderI18n setDescription(StringI18n description) {
        nest.setDescription(i18n(description));
        return this;
    }

    public EmbedBuilderI18n setFooter(StringI18n text) {
        nest.setFooter(i18n(text));
        return this;
    }

    public EmbedBuilderI18n setFooter(StringI18n text, String iconUrl) {
        nest.setFooter(i18n(text), iconUrl);
        return this;
    }

    public EmbedBuilderI18n setImage(String url) {
        nest.setImage(url);
        return this;
    }

    public EmbedBuilderI18n setThumbnail(String url) {
        nest.setThumbnail(url);
        return this;
    }

    public EmbedBuilderI18n setTimestamp(TemporalAccessor temporal) {
        nest.setTimestamp(temporal);
        return this;
    }

    public EmbedBuilderI18n setTitle(StringI18n title) {
        nest.setTitle(i18n(title));
        return this;
    }

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
