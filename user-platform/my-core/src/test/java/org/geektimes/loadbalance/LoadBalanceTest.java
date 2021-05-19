package org.geektimes.loadbalance;

import org.geektimes.loadbalance.consistenthash.*;
import org.geektimes.util.StatisticsUtil;
import org.geektimes.util.UuidUtils;

import java.util.*;

public class LoadBalanceTest {

    static String[] ips = {
            "10.27.127.67",
            "10.27.134.107",
            "10.23.58.112",
            "10.23.90.169",
            "10.24.58.112",
            "10.24.50.24",
            "10.23.120.8",
            "10.27.62.112",
            "10.27.78.248",
            "10.26.228.195",
            "10.26.240.203",
            "10.27.19.252",
            "10.23.91.19",
            "10.8.239.196",
            "10.10.147.202",
            "10.10.174.220",
            "10.17.110.6",
            "10.14.68.78",
            "10.17.110.108",
            "10.17.110.107",
            "10.21.132.41",
            "10.17.98.170",
            "10.13.166.82",
            "10.17.97.234",
            "10.14.69.38",
            "10.17.110.52",
            "10.27.61.119",
            "10.27.85.228",
            "10.224.244.121",
            "10.226.220.49"
    };

    public static void main(String[] args) {
        LoadBalanceTest test = new LoadBalanceTest();
        test.testDistribution(new MurmurHashStrategy());
        test.testDistribution(new FnvHashStrategy());
        test.testDistribution(new KetamaHashStrategy());
        test.testDistribution(new JdkHashCodeStrategy());
        test.testDistribution(new RSHashStrategy());
        test.testDistribution(null);
        test.testNodeAddAndRemove(new MurmurHashStrategy());
        test.testNodeAddAndRemove(new FnvHashStrategy());
        test.testNodeAddAndRemove(new KetamaHashStrategy());
        test.testNodeAddAndRemove(new JdkHashCodeStrategy());
        test.testNodeAddAndRemove(new RSHashStrategy());
        test.testNodeAddAndRemove(null);
    }

    public void testDistribution(HashStrategy hashStrategy) {
        List<Server> servers = new ArrayList<>();
        for (String ip : ips) {
            servers.add(new Server(ip + ":8080"));
        }

        String hashStrategyName = "改进的KetamaConsistentHash";
        LoadBalance loadBalance = new KetamaConsistentHashLoadBalancer(12);
        if (hashStrategy != null) {
            loadBalance = new ConsistenthashLoadBalancer(hashStrategy, 12);
            hashStrategyName = hashStrategy.getClass().getSimpleName();
        }

        Map<Server, Integer> serverCountMap = new HashMap<>();

        for (int i = 0; i < 10000; i++) {
            String key = UuidUtils.getUUID();
            Server server = loadBalance.select(servers, key);
            int count = serverCountMap.containsKey(server) ? serverCountMap.get(server) + 1 : 1;
            serverCountMap.put(server, count);
        }

        Integer[] iArray = new Integer[]{};
        Collection<Integer> values = serverCountMap.values();
        //标准差
        Double variance = StatisticsUtil.variance(values.toArray(iArray));
        //方差
        Double standardDeviation = StatisticsUtil.standardDeviation(values.toArray(iArray));
        System.out.printf("%s算法，方差：%f,  标准差为：%f \n", hashStrategyName, variance, standardDeviation);
    }

    public void testNodeAddAndRemove(HashStrategy hashStrategy) {
        List<Server> servers = new ArrayList<>();
        for (String ip : ips) {
            servers.add(new Server(ip + ":8080"));
        }
        List<Server> serversChanged = servers.subList(0, 24);

        String hashStrategyName = "改进的KetamaConsistentHash";
        LoadBalance loadBalance = new KetamaConsistentHashLoadBalancer(12);
        if (hashStrategy != null) {
            loadBalance = new ConsistenthashLoadBalancer(hashStrategy, 12);
            hashStrategyName = hashStrategy.getClass().getSimpleName();
        }

        int count = 0;

        for (int i = 0; i < 10000; i++) {
            String key = UuidUtils.getUUID();
            Server origin = loadBalance.select(servers, key);
            Server changed = loadBalance.select(serversChanged, key);
            if (origin.getUrl().equals(changed.getUrl()))
                count++;
        }

        System.out.println(hashStrategyName + "算法，不变流量比例为：" + (count / 10000D));
    }
}
