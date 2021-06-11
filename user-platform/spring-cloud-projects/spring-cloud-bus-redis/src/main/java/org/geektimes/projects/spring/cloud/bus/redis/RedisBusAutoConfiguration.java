package org.geektimes.projects.spring.cloud.bus.redis;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.bus.BusAutoConfiguration;
import org.springframework.cloud.bus.BusBridge;
import org.springframework.cloud.bus.BusProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(Jedis.class)
@AutoConfigureBefore(BusAutoConfiguration.class)
public class RedisBusAutoConfiguration {

    public static final String DEFAULT_REDIS_CONNECTION_STRING = "redis://localhost:6379";

    @Bean
    public RedisCacheManager redisCacheManager() {
        return new RedisCacheManager(DEFAULT_REDIS_CONNECTION_STRING);
    }


    @Bean
    @ConditionalOnMissingBean(BusBridge.class)
    public RedisBusBridge redisBusBridge(RedisCacheManager redisCacheManager, BusProperties busProperties) {
        return new RedisBusBridge(redisCacheManager, busProperties);
    }

}
