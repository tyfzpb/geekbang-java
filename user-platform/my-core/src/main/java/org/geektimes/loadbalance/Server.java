package org.geektimes.loadbalance;

public class Server {

    private String url;

    public Server() {
    }

    public Server(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
