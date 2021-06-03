## 第十三周作业说明：
### 基于文件系统为 Spring Cloud 提供 PropertySourceLocator实现
- 配置文件命名规则（META-INF/config/default.properties 或者 META-INF/config/default.yaml）
- 可选：实现文件修改通知


### 完成情况：
- 已实现。

  - spring-could-config-server模块
    - [FileSystemPropertySourceLocator](https://gitee.com/ty-fzpb/geekbang-java/tree/soa/user-platform/spring-cloud-projects/spring-cloud-config-server/src/main/java/org/geektimes/projects/spring/cloud/config/server/FileSystemPropertySourceLocator.java) 实现PropertySourceLocator  
      并且[META-INF/spring.factories](https://gitee.com/ty-fzpb/geekbang-java/tree/soa/user-platform/spring-cloud-projects/spring-cloud-config-server/src/main/resources/META-INF/spring.factories) 添加
      ```properties
      org.springframework.cloud.bootstrap.BootstrapConfiguration=\
      org.geektimes.projects.spring.cloud.config.server.FileSystemPropertySourceLocator  
      ```
    - 添加 bootstrap模块依赖，驱动BootstrapConfiguration装载。
      ```xml
      <dependency>
          <groupId>org.springframework.cloud</groupId>
          <artifactId>spring-cloud-starter-bootstrap</artifactId>
      </dependency>
      ```
    - [FileSystemPropertySourceRefresher](https://gitee.com/ty-fzpb/geekbang-java/tree/soa/user-platform/spring-cloud-projects/spring-cloud-config-server/src/main/java/org/geektimes/projects/spring/cloud/config/server/FileSystemPropertySourceRefresher.java) 监听文件修改，触发配置更新  。
    - 测试结果如下（手动修改文件触发）：
      ```shell
      user: User{name='ty', age='34'}
      user: User{name='ty', age='34'}
      user: User{name='ty', age='34'}
      user: User{name='ty', age='34'}
      default.properties on chage ENTRY_MODIFY
      2021-06-04 02:47:35.154  INFO 65276 --- [pool-9-thread-1] b.c.PropertySourceBootstrapConfiguration : Located property source: [BootstrapPropertySource {name='bootstrapProperties-fileSystemPropertySource'}]
      2021-06-04 02:47:35.156  INFO 65276 --- [pool-9-thread-1] o.s.boot.SpringApplication               : The following profiles are active: native,file
      2021-06-04 02:47:35.164  INFO 65276 --- [pool-9-thread-1] o.s.boot.SpringApplication               : Started application in 0.068 seconds (JVM running for 78.363)
      2021-06-04 02:47:35.261  INFO 65276 --- [pool-9-thread-1] com.netflix.discovery.DiscoveryClient    : Shutting down DiscoveryClient ...
      user: User{name='ty', age='32'}
      user: User{name='ty', age='32'}
      user: User{name='ty', age='32'}
      ```
  