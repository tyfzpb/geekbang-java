/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.geektimes.projects.spring.cloud.config.server;

import org.geektimes.projects.spring.cloud.config.server.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.boot.web.server.WebServer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.event.EventListener;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Spring Cloud Config Server 引导类
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
@SpringBootApplication
@EnableConfigServer // 激活 Config Server
@EnableDiscoveryClient
public class ConfigServer implements ApplicationRunner {
    @Autowired
    private User user;

    public static void main(String[] args) {
        SpringApplication.run(ConfigServer.class, args);
    }

    @EventListener(WebServerInitializedEvent.class)
    public void onWebServerInitialized(WebServerInitializedEvent event) {
        WebServer webServer = event.getWebServer();
        System.out.println("当前 Web 服务器端口：" + webServer.getPort());
    }

    public void run(ApplicationArguments args) throws Exception {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() ->
                        System.out.println("user: " + user),
                0, 1, TimeUnit.SECONDS);
    }


}
