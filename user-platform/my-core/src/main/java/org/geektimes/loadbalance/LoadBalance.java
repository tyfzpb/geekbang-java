package org.geektimes.loadbalance;

import java.util.List;

public interface LoadBalance {

    Server select(List<Server> servers, String key);

}
