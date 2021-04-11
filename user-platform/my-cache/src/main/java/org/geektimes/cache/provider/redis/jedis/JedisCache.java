package org.geektimes.cache.provider.redis.jedis;

import org.geektimes.cache.provider.AbstractCache;
import org.geetimes.util.SerializeUtil;
import redis.clients.jedis.Jedis;

import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.cache.configuration.Configuration;
import java.io.Serializable;
import java.util.Iterator;

public class JedisCache<K extends Serializable, V extends Serializable> extends AbstractCache<K, V> {


    private final Jedis jedis;

    public <C extends Configuration<K, V>> JedisCache(CacheManager cacheManager, String cacheName, C configuration, Jedis jedis) {
        super(cacheManager, cacheName, configuration);
        this.jedis = jedis;
    }

    @Override
    protected V doGet(K key) throws CacheException, ClassCastException {
        byte[] keyBytes = SerializeUtil.serialize(key);
        return doGet(keyBytes);
    }

    protected V doGet(byte[] bytes) {
        byte[] valueBytes = jedis.get(bytes);
        V value = (V) SerializeUtil.deserizlize(valueBytes);
        return value;
    }

    @Override
    protected V doPut(K key, V value) throws CacheException, ClassCastException {
        byte[] keyBytes = SerializeUtil.serialize(key);
        byte[] valueBytes = SerializeUtil.serialize(value);
        V oldValue = doGet(keyBytes);
        jedis.set(keyBytes, valueBytes);
        return oldValue;
    }

    @Override
    protected V doRemove(K key) throws CacheException, ClassCastException {
        byte[] keyBytes = SerializeUtil.serialize(key);
        V oldValue = doGet(keyBytes);
        jedis.del(keyBytes);
        return oldValue;
    }

    @Override
    protected void doClose() {
        this.jedis.close();
    }

    @Override
    protected void doClear() throws CacheException {

    }

    @Override
    protected Iterator<Entry<K, V>> newIterator() {
        return null;
    }
}
