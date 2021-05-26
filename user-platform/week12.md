## 第十二周作业说明：
### 将上次 MyBatis @Enable 模块驱动，封装成 Spring Boot Starter ⽅式
参考：MyBatis Spring Project ⾥⾯会有 Spring Boot 实现


### 完成情况：
- 已实现。

  - 新建my-mybatis-spring-boot模块，包含子模块
    - my-mybatis-spring-boot-starter模块 依赖autoconfigure模块，并且附件其他需的依赖
    - my-mybatis-spring-boot-autoconfigure模块  自动装配核心代码模块
    - my-mybatis-spring-boot-samples模块  主要用于测试验证  。
  - 详见my-mybatis-spring-boot-autoconfigure模块 org.geektimes.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration
      - 测试类 y-mybatis-spring-boot-samples模块 org.geektimes.mybatis.spring.boot.sample.SampleMybatisApplication
  