#第三周作业说明：
##需求一（必须）
- 整合 https://jolokia.org/
- 实现一个自定义 JMX MBean，通过 Jolokia 做 Servlet 代理

mvn clean package -U
java -jar user-web/target/user-web-v1-SNAPSHOT-war-exec.jar

访问http://localhost:8080/jolokia/read/org.geektimes.projects.user.management:type=User
查看结果

代码入口
org.geektimes.projects.user.web.listener.TestingListener#registerMBan

##需求二（选做）
- 继续完成 Microprofile config API 中的实现
- 扩展 
  org.eclipse.microprofile.config.spi.ConfigSource 实现，
  包括 OS 环境变量，以及本地配置文件
-扩展 org.eclipse.microprofile.config.spi.Converter 实现，
  提供 String 类型到简单类型
-通过 org.eclipse.microprofile.config.Config 读取当前应用名称
  - 应用名称 property name = "application.name"
    
代码路径：org/geektimes/configuration/microprofile/config
测试入口：test.org.geektimes.configuration.microprofile.config.TestJavaConfig