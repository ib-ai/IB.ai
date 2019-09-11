/* Copyright 2017-2019 Ray Clark
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

import de.arraying.gravity.GravityProvider;
import io.lettuce.core.RedisException;
import io.lettuce.core.api.sync.RedisCommands;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unchecked")
public final class DataProvider implements GravityProvider {
    private final RedisCommands sync;

    /**
     * Creates data provider object. Sets sync from Redis instance.
     */
    public DataProvider() {
        sync = DataContainer.INSTANCE.getSync();
    }

    @Override
    public String get(String key) {
        String value = null;
        try {
            value = sync.get(key).toString();
        } catch(RedisException re) {
            re.printStackTrace();
        } catch(NullPointerException npe) {
            return null;
        }
        return value;
    }

    @Override
    public void set(String key, String value) {
        try {
            sync.set(key, value);
        } catch(RedisException re) {
            re.printStackTrace();
        }
    }

    @Override
    public void del(String key) {
        try {
            sync.del(key);
        } catch(RedisException re) {
            re.printStackTrace();
        }
    }

    @Override
    public String hget(String key, String property) {
        String value = null;
        try {
            value = sync.hget(key, property).toString();
        } catch(RedisException re) {
            re.printStackTrace();
        }
        return value;
    }

    @Override
    public Set<String> hkeys(String key) {
        Set<String> keys = new HashSet<>();
        try {
            keys.addAll(sync.hkeys(key));
        } catch(RedisException re) {
            re.printStackTrace();
        }
        return keys;
    }

    @Override
    public void hset(String key, String property, String value) {
        try {
            sync.hset(key, property, value);
        } catch(RedisException re) {
            re.printStackTrace();
        }
    }

    @Override
    public void hdel(String key, String property) {
        try {
            sync.hdel(key, property);
        } catch(RedisException re) {
            re.printStackTrace();
        }
    }

    @Override
    public Set<String> smembers(String key) {
        Set<String> members = new HashSet<>();
        try {
            members.addAll(sync.smembers(key));
        } catch(RedisException re) {
            re.printStackTrace();
        }
        return members;
    }

    @Override
    public void sadd(String key, String value) {
        try {
            sync.sadd(key, value);
        } catch(RedisException re) {
            re.printStackTrace();
        }
    }

    @Override
    public void ladd(String key, String value) {
        try {
            sync.lpush(key, value);
        } catch(RedisException re) {
            re.printStackTrace();
        }
    }

    @Override
    public List<String> lrange(String key, int from, int to) {
        List<String> range = new ArrayList<>();
        try {
            range = sync.lrange(key, from, to);
        } catch(RedisException re) {
            re.printStackTrace();
        }
        return range;
    }

}
