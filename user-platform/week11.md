## 第十一周作业说明：
### 通过 Java 实现两种（以及）更多的一致性 Hash 算法
（可选）实现服务节点动态更新


### 完成情况：
- 已实现多种一致性hash算法。

  - 采用MurmurHash、RsHash、FNVHash、KetamaHash、JDKHashCode等多种hash算法实现一致性hash算法并进行测试比较。
  - 详见my-core模块 org/geektimes/loadbalance/consistenthash
      - 测试类 org/geektimes/loadbalance/LoadBalanceTest.java
  
  ```
  MurmurHashStrategy算法，方差：4485.422222,  标准差为：66.973295 
  FnvHashStrategy算法，方差：10326.688889,  标准差为：101.620317
  KetamaHashStrategy算法，方差：4227.822222,  标准差为：65.021706
  JdkHashCodeStrategy算法，方差：59471.955556,  标准差为：243.868726
  RSHashStrategy算法，方差：47655.622222,  标准差为：218.301677
  改进的KetamaConsistentHash算法，方差：4265.488889,  标准差为：65.310710
  MurmurHashStrategy算法，不变流量比例为：0.8151
  FnvHashStrategy算法，不变流量比例为：0.7967
  KetamaHashStrategy算法，不变流量比例为：0.7985
  JdkHashCodeStrategy算法，不变流量比例为：0.8772
  RSHashStrategy算法，不变流量比例为：0.8148
  改进的KetamaConsistentHash算法，不变流量比例为：0.8327
  ```

  - 1、从测试结果来看MurMurHashStrategy算法 和KetamaConsistentHash算法 标准差相对较小，即数据分布较均匀。
  - 2、移除部分server节点（下线20%）后，不变流量比例体现了服务器上下线对原有请求的影响程度，不变流量比例越高越高，可以发现 KetamaHashStrategy 和 MurmurHashStrategy 表现较为优秀。
