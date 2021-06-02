## 第十三周作业说明：
### 基于文件系统为 Spring Cloud 提供 PropertySourceLocator实现
- 配置文件命名规则（META-INF/config/default.properties 或者 META-INF/config/default.yaml）
- 可选：实现文件修改通知


### 完成情况：
- 已实现。

  - 新建my-mybatis-spring-boot模块，包含子模块
    - my-mybatis-spring-boot-starter模块 依赖autoconfigure模块，并且附件其他需的依赖
    - my-mybatis-spring-boot-autoconfigure模块  自动装配核心代码模块
    - my-mybatis-spring-boot-samples模块  主要用于测试验证  。
  - 详见my-mybatis-spring-boot-autoconfigure模块 org.geektimes.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration
      - 测试类 y-mybatis-spring-boot-samples模块 org.geektimes.mybatis.spring.boot.sample.SampleMybatisApplication
  