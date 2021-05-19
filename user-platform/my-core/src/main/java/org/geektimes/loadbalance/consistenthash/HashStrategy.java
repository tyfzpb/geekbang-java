package org.geektimes.loadbalance.consistenthash;

public interface HashStrategy {

    int getHash(String origin);

}
