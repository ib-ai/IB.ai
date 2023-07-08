/* Copyright 2020 Nathaneal Varghese
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

package com.ibdiscord.data.db;

public enum SQLQuery {
    /**
     * Creating schemas.
     */
    CREATE_SCHEMA_SERVER("CREATE SCHEMA IF NOT EXISTS server"),
    CREATE_SCHEMA_STAFF("CREATE SCHEMA IF NOT EXISTS staff"),
    CREATE_SCHEMA_HELPER("CREATE SCHEMA IF NOT EXISTS helper"),
    CREATE_SCHEMA_MEMBER("CREATE SCHEMA IF NOT EXISTS member"),

    /**
     * Declaring enums.
     */
    CREATE_TYPE_PUNISHMENT_TYPE("CREATE TYPE punishment_type AS ENUM ('KICK', 'MUTE', 'BAN', 'UNKNOWN');"),
    CREATE_TYPE_CHANNEL_TYPE("CREATE TYPE channel_type AS ENUM ('TEXT', 'VOICE');"),

    /**
     * Creating server tables.
     */
    CREATE_TABLE_SERVER_GUILD_DATA("CREATE TABLE IF NOT EXISTS server.guild_data ("
            + "guild_id BIGINT PRIMARY KEY,"
            + "prefix CHAR(1),"
            + "modlog_id TEXT,"
            + "updates_id TEXT,"
            + "logs_id TEXT,"
            + "mute_id TEXT,"
            + "moderator_id TEXT,"
            + "helper_id TEXT,"
            + "filtering BOOLEAN,"
            + "removal BOOLEAN,"
            + "monitoring TEXT,"
            + "monitor_user_log_id TEXT,"
            + "monitor_message_log_id TEXT);"),
    CREATE_TABLE_SERVER_FILTER("CREATE TABLE IF NOT EXISTS server.filter ("
            + "filter_id SERIAL PRIMARY KEY,"
            + "trigger TEXT,"
            + "notify BOOLEAN);"),
    CREATE_TABLE_SERVER_SNAPSHOT("CREATE TABLE IF NOT EXISTS server.snapshot ("
            + "category_id BIGINT PRIMARY KEY,"
            + "type CHANNEL_TYPE,"
            + "channel_list TEXT[]);"),
    CREATE_TABLE_SERVER_CASSOWARY("CREATE TABLE IF NOT EXISTS server.cassowary ("
            + "cassowary_id SERIAL PRIMARY KEY,"
            + "label TEXT,"
            + "penguin BOOLEAN);"),
    CREATE_TABLE_SERVER_CASSOWARY_ROLES("CREATE TABLE IF NOT EXISTS server.cassowary_roles ("
            + "cassowary_role_id SERIAL PRIMARY KEY,"
            + "role_id BIGINT,"
            + "cassowary_id INT REFERENCES server.cassowary (cassowary_id));"),

    /**
     * Creating staff tables.
     */
    CREATE_TABLE_STAFF_TAG("CREATE TABLE IF NOT EXISTS staff.tag ("
            + "tag_id SERIAL PRIMARY KEY,"
            + "trigger TEXT,"
            + "output TEXT,"
            + "disabled BOOLEAN);"),
    CREATE_TABLE_STAFF_NOTE("CREATE TABLE IF NOT EXISTS staff.note ("
            + "note_id SERIAL PRIMARY KEY,"
            + "user_id BIGINT,"
            + "author_id BIGINT,"
            + "timestamp TIMESTAMP,"
            + "data TEXT);"),
    CREATE_TABLE_STAFF_VOTE_LADDER("CREATE TABLE IF NOT EXISTS staff.vote_ladder ("
            + "ladder_id SERIAL PRIMARY KEY,"
            + "ladder_label TEXT,"
            + "channel_id TEXT,"
            + "threshold INT,"
            + "timeout INT);"),
    CREATE_TABLE_STAFF_VOTE("CREATE TABLE IF NOT EXISTS staff.vote ("
            + "vote_id SERIAL PRIMARY KEY,"
            + "ladder_number INT,"
            + "message TEXT,"
            + "positive INT,"
            + "negative INT,"
            + "expiry INT,"
            + "finished BOOLEAN,"
            + "ladder_id INT REFERENCES staff.vote_ladder (ladder_id));"),
    CREATE_TABLE_STAFF_MONITOR_USER("CREATE TABLE IF NOT EXISTS staff.monitor_user ("
            + "monitor_user_id SERIAL PRIMARY KEY,"
            + "user_id BIGINT);"),
    CREATE_TABLE_STAFF_MONITOR_MESSAGE("CREATE TABLE IF NOT EXISTS staff.monitor_message ("
            + "monitor_message_id SERIAL PRIMARY KEY,"
            + "message TEXT);"),
    CREATE_TABLE_STAFF_REACTION("CREATE TABLE IF NOT EXISTS staff.reaction ("
            + "reaction_id SERIAL PRIMARY KEY,"
            + "channel_id TEXT,"
            + "message_id TEXT,"
            + "emoji_id TEXT);"),
    CREATE_TABLE_STAFF_REACTION_ROLE("CREATE TABLE IF NOT EXISTS staff.reaction_role ("
            + "reaction_role_id SERIAL PRIMARY KEY,"
            + "role_id BIGINT,"
            + "positive BOOLEAN,"
            + "reaction_id INT REFERENCES staff.reaction (reaction_id));"),
    CREATE_TABLE_STAFF_PUNISHMENT("CREATE TABLE IF NOT EXISTS staff.punishment ("
            + "punishment_id SERIAL PRIMARY KEY,"
            + "case_id INT,"
            + "type PUNISHMENT_TYPE,"
            + "user_display TEXT,"
            + "user_id BIGINT,"
            + "staff_display TEXT,"
            + "staff_id BIGINT,"
            + "reason TEXT,"
            + "redacted BOOLEAN,"
            + "message_id TEXT,"
            + "expiry INT);"),

    /**
     * Creating helper tables.
     */
    CREATE_TABLE_HELPER_DATA("CREATE TABLE IF NOT EXISTS helper.helper_data ("
            + "user_id BIGINT PRIMARY KEY, "
            + "inactive BOOLEAN);"),
    CREATE_TABLE_HELPER_MESSAGE("CREATE TABLE IF NOT EXISTS helper.helper_message ("
            + "helper_message_id SERIAL PRIMARY KEY,"
            + "channel_id BIGINT,"
            + "message_id BIGINT,"
            + "role_id BIGINT);"),

    /**
     * Creating member tables.
     */
    CREATE_TABLE_MEMBER_DATA("CREATE TABLE IF NOT EXISTS member.member_data ("
            + "user_id BIGINT PRIMARY KEY,"
            + "join_override TEXT,"
            + "lang_code TEXT);"),
    CREATE_TABLE_MEMBER_ROLE("CREATE TABLE IF NOT EXISTS member.member_role ("
            + "user_role_id SERIAL PRIMARY KEY,"
            + "user_id BIGINT,"
            + "role_id BIGINT);"),
    CREATE_TABLE_MEMBER_OPT("CREATE TABLE IF NOT EXISTS member.member_opt ("
            + "opt_id SERIAL PRIMARY KEY,"
            + "user_id BIGINT,"
            + "channel_id BIGINT);"),
    CREATE_TABLE_MEMBER_REMINDER("CREATE TABLE IF NOT EXISTS member.member_reminder ("
            + "reminder_id SERIAL PRIMARY KEY,"
            + "text TEXT,"
            + "time INT,"
            + "user_id BIGINT);");

    private String query;

    SQLQuery(String query) {
        this.query = query;
    }

    @Override
    public String toString() {
        return query;
    }
}
