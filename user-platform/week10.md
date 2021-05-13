## 第十周作业说明：
### @org.geektimes.projects.user.mybatis.annotation.EnableMyBatis 实现，尽可能多地注入org.mybatis.spring.SqlSessionFactoryBean 中依赖的组件


### 完成情况：
- 已实现。

  - 1、增加typeAliasesPackage、typeHandlersPackage、cache、transactionFactory、sqlSessionFactory、failFast等常用属性实现。
  - 2、增加
    ```java
      @AliasFor(annotation = MapperScan.class)
      String[] basePackages();
    ```
      实现@MapperScan中basePackages属性覆盖，使用时只需要@EnableMyBatis一个注解。
    - 详见spring-user-web模块
      - 测试类 org/geektimes/spring/mybatis/EnableMyBatisTest.java