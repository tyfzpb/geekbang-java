package org.geektimes.cache.provider.redis.lettuce;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.geektimes.cache.provider.AbstractCache;
import org.geektimes.cache.provider.redis.lettuce.codec.DefaultRedisCodec;

import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.cache.configuration.Configuration;
import java.io.Serializable;
import java.util.Iterator;

public class LettuceCache<K extends Serializable, V extends Serializable> extends AbstractCache<K, V> {

    private final StatefulRedisConnection<K, V> statefulRedisConnection;

    private final RedisCommands<K, V> redisCommands;


    public <C extends Configuration<K, V>> LettuceCache(CacheManager cacheManager, String cacheName, C configuration, RedisClient redisClient) {
        super(cacheManager, cacheName, configuration);
        this.statefulRedisConnection = redisClient.connect(new DefaultRedisCodec(configuration.getKeyType(), configuration.getValueType()));
        this.redisCommands = statefulRedisConnection.sync();
    }

    @Override
    protected V doGet(K key) throws CacheException, ClassCastException {
        return redisCommands.get(key);
    }


    @Override
    protected V doPut(K key, V value) throws CacheException, ClassCastException {
        V oldValue = doGet(key);
        redisCommands.set(key, value);
        return oldValue;
    }

    @Override
    protected V doRemove(K key) throws CacheException, ClassCastException {
        V oldValue = doGet(key);
        redisCommands.del(key);
        return oldValue;
    }

    @Override
    protected void doClose() {
        this.statefulRedisConnection.close();
    }

    @Override
    protected void doClear() throws CacheException {

    }

    @Override
    protected Iterator<Entry<K, V>> newIterator() {
        return null;
    }
}
