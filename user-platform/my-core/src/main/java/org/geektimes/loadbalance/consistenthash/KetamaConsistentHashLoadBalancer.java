package org.geektimes.loadbalance.consistenthash;

import org.geektimes.loadbalance.LoadBalance;
import org.geektimes.loadbalance.Server;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class KetamaConsistentHashLoadBalancer implements LoadBalance {


    private static final String VIRTUAL_NODE_SUFFIX = "##";
    private final int virtualNodeSize;

    public KetamaConsistentHashLoadBalancer() {
        this.virtualNodeSize = 10;
    }

    public KetamaConsistentHashLoadBalancer(int virtualNodeSize) {
        this.virtualNodeSize = virtualNodeSize;
    }

    @Override
    public Server select(List<Server> servers, String key) {
        long keyHash = getHash(key, 0);
        TreeMap<Long, Server> ring = buildConsistentHashRing(servers);
        Map.Entry<Long, Server> locateEntry = ring.ceilingEntry(keyHash);
        if (locateEntry == null)
            locateEntry = ring.firstEntry();
        return locateEntry.getValue();
    }

    private TreeMap<Long, Server> buildConsistentHashRing(List<Server> servers) {
        TreeMap<Long, Server> virtualNodeRing = new TreeMap<>();
        for (Server server : servers) {
            for (int i = 0; i < virtualNodeSize / 4; i++) {
                for (int h = 0; h < 4; h++) {
                    Long hashKey = getHash(server.getUrl() + VIRTUAL_NODE_SUFFIX + i, h);
                    virtualNodeRing.put(hashKey, server);
                }
            }
        }
        return virtualNodeRing;
    }


    public long getHash(String origin, int number) {
        MessageDigest md5Digest = null;
        try {
            md5Digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 not supported", e);
        }
        md5Digest.update(origin.getBytes(StandardCharsets.UTF_8));
        byte[] keyBtyes = md5Digest.digest();
        long rv = ((long) (keyBtyes[3 + number * 4] & 0xFF) << 24)
                | ((long) (keyBtyes[2 + number * 4] & 0xFF) << 16)
                | ((long) (keyBtyes[1 + number * 4] & 0xFF) << 8)
                | (keyBtyes[number * 4] & 0xFF);
        return rv;
    }
}
