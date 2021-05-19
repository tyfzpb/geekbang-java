package org.geektimes.loadbalance.consistenthash;

import org.geektimes.loadbalance.LoadBalance;
import org.geektimes.loadbalance.Server;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ConsistenthashLoadBalancer implements LoadBalance {
    private static final String VIRTUAL_NODE_SUFFIX = "##";
    private final HashStrategy hashStrategy;
    private final int virtualNodeSize;

    public ConsistenthashLoadBalancer() {
        this.hashStrategy = new FnvHashStrategy();
        this.virtualNodeSize = 10;
    }

    public ConsistenthashLoadBalancer(HashStrategy hashStrategy) {
        this.hashStrategy = hashStrategy;
        this.virtualNodeSize = 10;
    }

    public ConsistenthashLoadBalancer(HashStrategy hashStrategy, int virtualNodeSize) {
        this.hashStrategy = hashStrategy;
        this.virtualNodeSize = virtualNodeSize;
    }

    @Override
    public Server select(List<Server> servers, String key) {
        int keyHash = hashStrategy.getHash(key);
        TreeMap<Integer, Server> ring = buildConsistentHashRing(servers);
        Map.Entry<Integer, Server> locateEntry = ring.ceilingEntry(keyHash);
        if (locateEntry == null)
            locateEntry = ring.firstEntry();
        return locateEntry.getValue();
    }

    private TreeMap<Integer, Server> buildConsistentHashRing(List<Server> servers) {
        TreeMap<Integer, Server> virtualNodeRing = new TreeMap<>();
        for (Server server : servers) {
            for (int i = 0; i < virtualNodeSize; i++) {
                Integer hashKey = hashStrategy.getHash(server.getUrl() + VIRTUAL_NODE_SUFFIX + i);
                virtualNodeRing.put(hashKey, server);
            }
        }
        return virtualNodeRing;
    }

}
