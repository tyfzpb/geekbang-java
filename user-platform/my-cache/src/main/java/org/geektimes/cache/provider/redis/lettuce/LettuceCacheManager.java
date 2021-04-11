package org.geektimes.cache.provider.redis.lettuce;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import org.geektimes.cache.provider.AbstractCacheManager;

import javax.cache.Cache;
import javax.cache.configuration.Configuration;
import javax.cache.spi.CachingProvider;
import java.net.URI;
import java.util.Properties;

public class LettuceCacheManager extends AbstractCacheManager {

    private final RedisClient redisClient;


    public LettuceCacheManager(CachingProvider cachingProvider, URI uri, ClassLoader classLoader, Properties properties) {
        super(cachingProvider, uri, classLoader, properties);
//        String host = properties.getProperty("redis.host");
//        int port = Integer.valueOf(properties.getProperty("redis.port"));
//        String user = properties.getProperty("redis.user");
//        String password = properties.getProperty("redis.password");

//        RedisURI redisUri = RedisURI.create("redis://" + password + "@" + host + ":" + port + "/0?timeout=60s");

        this.redisClient = RedisClient.create(uri.toString());
    }

    @Override
    protected <K, V, C extends Configuration<K, V>> Cache doCreateCache(String cacheName, C configuration) {
        return new LettuceCache(this, cacheName, configuration, redisClient);
    }

    @Override
    protected void doClose() {
        redisClient.shutdown();
    }


}
