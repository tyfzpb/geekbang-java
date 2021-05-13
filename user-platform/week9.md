## 第九周作业说明：
### Spring Cache 与 Redis 整合

- 如何清除某个 Spring Cache 所有的 Keys 关联的对象
  - 如果 Redis 中心化方案，Redis + Sentinel
  - 如果 Redis 去中心化方案，Redis Cluster
- 如何将 RedisCacheManager 与 @Cacheable 注解打通
### 完成情况：
- 已实现。

  - clean() : 保存时以cacheName为key存储Keys的Set Value值，清除时，通过cacheName获取所以Kyes，然后del；
  - RedisCacheManager 与 @Cacheable 注解打通 见测试类CacheableTest
  - 详见my-spring-user-web模块