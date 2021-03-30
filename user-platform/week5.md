## 第四周作业说明：
### 1. 修复本程序 org.geektimes.reactive.streams 包下
### 完成情况：
- 已完成，将接收输出语句提前。

### 2. 继续完善 my-rest-client POST 方法 

### 完成情况：
  - 参考GET方法处理，POST特别之处在于RequestBody。
```java
 if (this.entity != null) {
    connection.setDoOutput(true);
    connection.setDoInput(true);
    connection.setUseCaches(false);
    connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
    connection.connect();
    Object body = entity.getEntity();
    OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8);
    writer.write(body.toString());
    writer.close();
}
```
详见my-rest-client HttpPostInvovation#invoke方法

- 测试类RestClientDemo