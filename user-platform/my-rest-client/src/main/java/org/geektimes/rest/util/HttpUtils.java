package org.geektimes.rest.util;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;

public class HttpUtils {

    private static final Client client = ClientBuilder.newClient();

    /**
     * POST 请求
     *
     * @param url URL
     * @return 结果
     */
    public static String post(String url) {
        return client.target(url).request().post(null,String.class);
    }

    /**
     * POST 请求
     *
     * @param url  URL
     * @param data JSON 参数
     * @return 结果
     */
    public static String post(String url, String data) {
        return client.target(url).request().post(Entity.json(data), String.class);
    }

    public static String get(String url) {
        return client.target(url).request().get(String.class);
    }

    public static String get(String url,String headerName,Object headerObject){
        return client.target(url).request().header(headerName,headerObject).get(String.class);
    }
}
