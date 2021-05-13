package org.geektimes.spring.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@EnableCaching
@Configuration
@PropertySource(value = "application.properties",encoding = "UTF-8")
public class CacheManagerConfig {

    @Bean
    public CacheManager cacheManager(@Value("${redis.url}") String url) {
        return new RedisCacheManager(url);
    }
}
