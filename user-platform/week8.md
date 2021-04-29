## 第七周作业说明：
### 如何解决多个 WebSecurityConfigurerAdapter Bean 配置相互冲突的问题？

- 提示：假设有两个 WebSecurityConfigurerAdapter Bean 定义，并且标注了不同的 @Order，其中一个关闭 CSRF，一个开启 CSRF，那么最终结果如何确定？

- 背景：Spring Boot 场景下，自动装配以及自定义 Starter 方式非常流行，部分开发人员掌握了 Spring Security 配置方法，并且自定义了自己的实现，解决了 Order 的问题，然而会出现不确定配置因素。
### 完成情况：
- 已实现。

  - 思路：通过自定义IWebSecurityConfigurer接口作为扩展，用MyWebSecurityConfigurerAdapter统一处理(HttpSecurity http)
  - 详见spring-security模块