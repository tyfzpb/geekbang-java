## 第六周作业说明：
### 1. 提供一套抽象API实现对象的序列化和反序列化
### 完成情况：
- 已完成，详见user-platform/my-cache/src/main/java/org/geektimes/cache/serializer

### 2. 通过Lettuce实现一套Redis CacheManager以及Cache

### 完成情况：
  - Lettuce使用的时候依赖于四个主要组件：
    * RedisURI：链接信息。
    * RedisClient：Redis客户端，特殊地，集群链接有一个定制的RedisClusterClient。
    * Connection：Redis链接，主要是StatefulConnection或者StatefulRedisConnection的子类，链接的类型主要由链接的具体方式（单机、哨兵、集群、订阅发布等等）选定，比较重要。
    * RedisCommands：Redis命令API接口，基本上覆盖了Redis发行版本的全部命令，提供了同步（sync）、异步（async）、反应式（reative）的调用方式，对于使用者而言，会常常跟RedisCommands系列接口打交道。

  - 在不指定编码解码器RedisCodec的前提下，RedisClient建立的StatefulRedisConnection实例通常是泛型实例StatefulRedisConnection<String,String>，也就是全部命令API的KEY和VALUE都是String类型，这种使用方式能满足大部分的使用场景。然而，必要的时候能够定制编码解码器RedisCodec<K,V>。
    在这里我们就实现了自定义的编解码器：org.geektimes.cache.provider.redis.lettuce.codec.GenericRedisCodec
    * 提供常用类型String，Integer，Double，Float，Long，Short，Byte编解码以及可序列化对象的编解码

```java
 public <C extends Configuration<K, V>> LettuceCache(CacheManager cacheManager, String cacheName, C configuration, RedisClient redisClient) {
        super(cacheManager, cacheName, configuration);
        this.statefulRedisConnection = redisClient.connect(new GenericRedisCodec(configuration.getKeyType(), configuration.getValueType()));
        this.redisCommands = statefulRedisConnection.sync();
        }
```
详见my-cache LettuceCache以及GenericRedisCodec

- 测试类CachingTest#testSampleLettuce方法 普通类型key-value
  *  CachingTest#testLettuceUser方法 