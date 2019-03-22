package com.ibdiscord.data.db;

import de.arraying.gravity.GravityProvider;
import io.lettuce.core.RedisException;
import io.lettuce.core.api.sync.RedisCommands;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Copyright 2019 Ray Clark
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
@SuppressWarnings("unchecked")
final class DProvider implements GravityProvider {
    private final RedisCommands sync;

    DProvider() {
        sync = DContainer.INSTANCE.getSync();
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
