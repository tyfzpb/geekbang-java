## 第四周作业说明：
### 1. 完善 my dependency-injection 
- 模块脱离 web.xml 配置实现 ComponentContext 自动初始化
- 使用独立模块并且能够在 user-web 中运行成功

### 完成情况：
- 已完成，正常注册即可。

### 2. 完善 my-configuration 模块 
   
- Config 对象如何能被 my-web-mvc 使用
  - 可能在 ServletContext 
  - 获取如何通过 ThreadLocal 获取

### 完成情况：
  - 测试入口：
    - my-configuration模块 org.geektimes.configuration.microprofile.config.TestJavaConfig
    - user-web模块 org.geektimes.projects.user.web.controller.HelloWorldController.execute
