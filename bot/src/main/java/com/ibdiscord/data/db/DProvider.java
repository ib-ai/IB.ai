package com.ibdiscord.data.db;

import de.arraying.gravity.GravityProvider;
import io.lettuce.core.api.sync.RedisCommands;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DProvider implements GravityProvider {
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
