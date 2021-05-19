package org.geektimes.loadbalance.consistenthash;

/**
 *
 * FNV1_32_HASH 算法
 *
 * @author  ty_fzpb
 */
public class FnvHashStrategy implements HashStrategy {

    @Override
    public int getHash(String origin) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < origin.length(); i++)
            hash = (hash ^ origin.charAt(i)) * p;
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        if (hash < 0)
            hash = Math.abs(hash);
        return hash;
    }
}
