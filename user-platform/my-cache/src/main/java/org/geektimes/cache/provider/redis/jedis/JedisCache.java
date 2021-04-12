package org.geektimes.cache.provider.redis.jedis;

import org.geektimes.cache.ExpirableEntry;
import org.geektimes.cache.provider.AbstractCache;
import org.geektimes.cache.serializer.ByteArraySerializer;
import org.geektimes.cache.serializer.CacheSerializer;
import redis.clients.jedis.Jedis;

import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.cache.configuration.Configuration;
import java.io.Serializable;
import java.util.Iterator;

public class JedisCache<K extends Serializable, V extends Serializable> extends AbstractCache<K, V> {

    private final CacheSerializer<Object, byte[]> serializer;

    private final Jedis jedis;

    public <C extends Configuration<K, V>> JedisCache(CacheManager cacheManager, String cacheName, C configuration, Jedis jedis) {
        super(cacheManager, cacheName, configuration);
        this.jedis = jedis;
        this.serializer = new ByteArraySerializer();
    }


    @Override
    protected boolean containsEntry(K key) throws CacheException, ClassCastException {
        byte[] keyBytes = serializer.serialize(key);
        return jedis.exists(keyBytes);
    }

    @Override
    protected ExpirableEntry<K, V> getEntry(K key) throws CacheException, ClassCastException {
        byte[] keyBytes = serializer.serialize(key);
        return getEntry(keyBytes);
    }

    protected ExpirableEntry<K, V> getEntry(byte[] keyBytes) throws CacheException, ClassCastException {
        byte[] valueBytes = jedis.get(keyBytes);
        return ExpirableEntry.of((K) serializer.deserialize(keyBytes), (V) serializer.deserialize(valueBytes));
    }

    @Override
    protected void putEntry(ExpirableEntry<K, V> newEntry) throws CacheException, ClassCastException {
        byte[] keyBytes = serializer.serialize(newEntry.getKey());
        byte[] valueBytes = serializer.serialize(newEntry.getValue());
        jedis.set(keyBytes, valueBytes);
    }

    @Override
    protected ExpirableEntry<K, V> removeEntry(K key) throws CacheException, ClassCastException {
        byte[] keyBytes = serializer.serialize(key);
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
