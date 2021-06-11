/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.geektimes.projects.spring.cloud.bus.redis;

import org.springframework.cache.Cache;
import redis.clients.jedis.Jedis;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;

import static org.geektimes.util.SerializeUtil.deserialize;
import static org.geektimes.util.SerializeUtil.serialize;

/**
 * Redis Cache 实现
 *
 * @since 1.0.0
 * Date : 2021-04-29
 */
public class RedisCache implements Cache {

    private final String name;

    private final Jedis jedis;

    private final byte[] keySetKey;

    public RedisCache(String name, Jedis jedis) {
        Objects.requireNonNull(name, "The 'name' argument must not be null.");
        Objects.requireNonNull(jedis, "The 'jedis' argument must not be null.");
        this.name = name;
        this.jedis = jedis;
        keySetKey = serialize(name);
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getNativeCache() {
        return jedis;
    }

    @Override
    public ValueWrapper get(Object key) {
        byte[] keyBytes = serialize(key);
        byte[] valueBytes = jedis.get(keyBytes);
        if (valueBytes == null)
            return null;
        return () -> deserialize(valueBytes);
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        return null;
    }


    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        return null;
    }

    @Override
    public void put(Object key, Object value) {
        byte[] keyBytes = serialize(key);
        byte[] valueBytes = serialize(value);
        jedis.set(keyBytes, valueBytes);
        saveKeySet(key);
    }

    private void saveKeySet(Object key) {
        byte[] valueBytes = jedis.get(keySetKey);
        Set<Object> keySet = new HashSet<>();
        if (valueBytes != null)
            keySet = (Set<Object>) deserialize(valueBytes);
        keySet.add(key);
        byte[] newValueBytes = serialize(keySet);
        jedis.set(keySetKey, newValueBytes);
    }

    @Override
    public void evict(Object key) {
        byte[] keyBytes = serialize(key);
        jedis.del(keyBytes);
    }

    @Override
    public void clear() {
        byte[] valueBytes = jedis.get(keySetKey);
        Set<Object> keySet = new HashSet<>();
        if (valueBytes != null)
            keySet = (Set<Object>) deserialize(valueBytes);
        keySet.stream().map(key -> serialize(key)).forEach(jedis::del);
        jedis.del(keySetKey);
    }


}
