package org.geektimes.mybatis.spring.boot.autoconfigure;

import org.geektimes.mybatis.spring.annotation.EnableMybatis;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ConditionalOnSingleCandidate(DataSource.class)
@ConditionalOnProperty(prefix = "mybatis", name = "enabled", havingValue = "true", matchIfMissing = true)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class MybatisAutoConfiguration {

    private final static String mybatisBasePackages = "org.geektimes.mybatis.spring";
    //Attribute value must be constant


    @Configuration
    @EnableMybatis(basePackages = {mybatisBasePackages})
    static class MybatisConfiguration {

    }

}
