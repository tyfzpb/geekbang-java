package org.geektimes.spring.mybatis;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;


@Configuration
@PropertySource(value = "application.properties",encoding = "UTF-8")
public class DatabaseConfig {

    @Bean
    public DataSource demoDataSource(@Value("${jdbc.driver}") String driver, @Value("${jdbc.url}") String url, @Value("${jdbc.username}") String username, @Value("${jdbc.password}") String password) {
        return new PooledDataSource(driver, url, username, password);
    }
}
