/**
 * Copyright 2018 raynichc
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

/**
 * @author raynichc
 * @since 2018.11.29
 */

package com.ibdiscord.data.db;

import de.arraying.gravity.GravityProvider;

import io.lettuce.core.api.sync.RedisCommands;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class DProvider implements GravityProvider {
    private final RedisCommands sync;

    public DProvider() {
        sync = DContainer.getSync();
    }

    @Override
    public String get(String key) {
        Object value = sync.get(key);
        return value == null ? null : value.toString();
    }

    @Override
    public void set(String key, String value) {
        sync.set(key, value);
    }

    @Override
    public void del(String key) {
        sync.del(key);
    }

    @Override
    public String hget(String key, String property) {
        Object value = sync.hget(key, property);
        return value == null ? null : value.toString();
    }

    @Override
    public Set<String> hkeys(String key) {
        Set<String> keys = new HashSet<String>(sync.hkeys(key));
        return keys;
    }

    @Override
    public void hset(String key, String property, String value) {
        sync.hset(key, property, value);
    }

    @Override
    public void hdel(String key, String property) {
        sync.hdel(key, property);
    }

    @Override
    public Set<String> smembers(String key) {
        Set<String> members = new HashSet<String>(sync.smembers(key));
        return members;
    }

    @Override
    public void sadd(String key, String value) {
        sync.sadd(key, value);
    }

    @Override
    public void ladd(String key, String value) {
        sync.lpush(key, value);
    }

    @Override
    public List<String> lrange(String key, int from, int to) {
        List<String> range = sync.lrange(key, from, to);
        return range;
    }
}
