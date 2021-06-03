package org.geektimes.projects.spring.cloud.config.server;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileSystemConfigAutoConfiguration {

    @ConditionalOnMissingBean
    @Bean
    public FileSystemPropertySourceRefresher fileSystemPropertySourceWatcher(FileSystemPropertySourceLocator locator){
        return new FileSystemPropertySourceRefresher(locator);
    }
}
