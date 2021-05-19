package org.geektimes.loadbalance.consistenthash;

/**
 * JDK hashCode 算法
 *
 * @author ty_fzpb
 */
public class JdkHashCodeStrategy implements HashStrategy {

    @Override
    public int getHash(String origin) {
        return origin.hashCode();
    }
}
