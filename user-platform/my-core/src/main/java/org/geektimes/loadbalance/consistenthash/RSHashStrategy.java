package org.geektimes.loadbalance.consistenthash;

/**
 *
 * RS_HASH 算法
 *
 * @author  ty_fzpb
 */
public class RSHashStrategy implements HashStrategy {

    @Override
    public int getHash(String origin) {
        int b = 378551;
        int a = 63689;
        int hash = 0;
        for (int i = 0; i < origin.length(); i++) {
            hash = hash * a + origin.charAt(i);
            a = a * b;
        }
        return (hash & 0x7FFFFFFF);
    }
}
