package org.geektimes.projects.spring.cloud.bus.redis;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.bus.BusBridge;
import org.springframework.cloud.bus.BusProperties;
import org.springframework.cloud.bus.event.EnvironmentChangeRemoteApplicationEvent;
import org.springframework.cloud.bus.event.RemoteApplicationEvent;
import redis.clients.jedis.Jedis;

import java.util.Map;

public class RedisBusBridge implements BusBridge {

    private final RedisCacheManager redisCacheManager;

    private final BusProperties properties;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public RedisBusBridge(RedisCacheManager redisCacheManager, BusProperties properties) {
        this.redisCacheManager = redisCacheManager;
        this.properties = properties;
    }

    @Override
    public void send(RemoteApplicationEvent event) {
        String destination = properties.getDestination();
        destination = destination == null ? "defaultRedisData" : destination;
        Jedis jedis = (Jedis) redisCacheManager.getMissingCache(destination).getNativeCache();

        Map<String, String> map = ((EnvironmentChangeRemoteApplicationEvent) event).getValues();
        try {
            String jsonData = objectMapper.writeValueAsString(map);
            jedis.publish(destination, jsonData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
