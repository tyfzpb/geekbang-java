package org.geektimes.cache.provider.redis.jedis;

import org.geektimes.cache.ExpirableEntry;
import org.geektimes.cache.provider.AbstractCache;
import redis.clients.jedis.Jedis;

import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.cache.configuration.Configuration;
import java.io.Serializable;
import java.util.Iterator;

import static org.geetimes.util.SerializeUtil.deserialize;
import static org.geetimes.util.SerializeUtil.serialize;

public class JedisCache<K extends Serializable, V extends Serializable> extends AbstractCache<K, V> {


    private final Jedis jedis;

    public <C extends Configuration<K, V>> JedisCache(CacheManager cacheManager, String cacheName, C configuration, Jedis jedis) {
        super(cacheManager, cacheName, configuration);
        this.jedis = jedis;
    }


    @Override
    protected boolean containsEntry(K key) throws CacheException, ClassCastException {
        byte[] keyBytes = serialize(key);
        return jedis.exists(keyBytes);
    }

    @Override
    protected ExpirableEntry<K, V> getEntry(K key) throws CacheException, ClassCastException {
        byte[] keyBytes = serialize(key);
        return getEntry(keyBytes);
    }

    protected ExpirableEntry<K, V> getEntry(byte[] keyBytes) throws CacheException, ClassCastException {
        byte[] valueBytes = jedis.get(keyBytes);
        return ExpirableEntry.of((K) deserialize(keyBytes), (V) deserialize(valueBytes));
    }

    @Override
    protected void putEntry(ExpirableEntry<K, V> newEntry) throws CacheException, ClassCastException {
        byte[] keyBytes = serialize(newEntry.getKey());
        byte[] valueBytes = serialize(newEntry.getValue());
        jedis.set(keyBytes, valueBytes);
    }

    @Override
    protected ExpirableEntry<K, V> removeEntry(K key) throws CacheException, ClassCastException {
        byte[] keyBytes = serialize(key);
        ExpirableEntry<K, V> oldEntry = getEntry(keyBytes);
        jedis.del(keyBytes);
        return oldEntry;
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
