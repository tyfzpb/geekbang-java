package org.geektimes.projects.spring.cloud.bus.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import redis.clients.jedis.JedisPubSub;

import java.util.Map;
import java.util.Properties;

@Configuration(proxyBeanMethods = false)
public class RedisMessagePubSubListener extends JedisPubSub implements ApplicationContextAware {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private ApplicationContext applicationContext;

    public void onMessage(String channel, String message) {
        System.out.println("onMessage: [channel :" + channel + ", message : " + message + "]");
        Map<String, Object> map = null;
        try {
            map = objectMapper.readValue(message, Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        ConfigurableEnvironment environment = (ConfigurableEnvironment) applicationContext.getEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources();
        Properties properties = new Properties();
        for (String key : map.keySet()) {
            properties.put(key, map.get(key));
        }
        propertySources.addFirst(new PropertiesPropertySource("defaultProperties", properties));
    }

    @Override
    public void onPMessage(String pattern, String channel, String message) {
        System.out.println("onPMessage: [pattern : " + pattern + ", channel :" + channel + ", message : " + message + "]");
    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        System.out.println("onSubscribe: [channel :" + channel + ", subscribedChannels : " + subscribedChannels + "]");
    }

    @Override
    public void onPUnsubscribe(String pattern, int subscribedChannels) {
        System.out.println("onPUnsubscribe: [pattern :" + pattern + ", subscribedChannels : " + subscribedChannels + "]");
    }

    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {
        System.out.println("onPSubscribe: [pattern :" + pattern + ", subscribedChannels : " + subscribedChannels + "]");
    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        System.out.println("onUnsubscribe: [channel :" + channel + ", subscribedChannels : " + subscribedChannels + "]");
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
