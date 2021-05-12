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
package org.geektimes.spring.mybatis.annotation;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import java.util.Map;

import static org.springframework.beans.factory.support.BeanDefinitionBuilder.genericBeanDefinition;

/**
 * @author ty_fzpb
 * @since 1.0.0
 * Date : 2021-05-06
 */
public class MyBatisBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private Environment environment;

    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<String, Object> attributes = importingClassMetadata.getAnnotationAttributes(EnableMyBatis.class.getName());
        AnnotationAttributes mapperScanAttrs = AnnotationAttributes.fromMap(attributes);
        registerSqlSessionFactoryBean(mapperScanAttrs, registry);
    }


    /***
     * register SqlSessionFactoryBean
     * @param attributes
     * @param registry
     * @See SqlSessionFactoryBean
     */
    private void registerSqlSessionFactoryBean(AnnotationAttributes attributes, BeanDefinitionRegistry registry) {
        BeanDefinitionBuilder beanDefinitionBuilder = genericBeanDefinition(SqlSessionFactoryBean.class);
        /**
         *  <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
         *   <property name="dataSource" ref="dataSource" />
         *   <property name="mapperLocations" value="classpath*:" />
         *  </bean >
         */
        beanDefinitionBuilder.addPropertyReference("dataSource", (String) attributes.get("dataSource"));
        // Spring String 类型可以自动转化 Spring Resource
        if (attributes.getStringArray("mapperLocations").length > 0) {
            beanDefinitionBuilder.addPropertyValue("mapperLocations", attributes.getStringArray("mapperLocations"));
        }
        if (StringUtils.hasText(attributes.getString("environment"))) {
            beanDefinitionBuilder.addPropertyValue("environment", resolvePlaceholder(attributes.get("environment")));
        }
        if (StringUtils.hasText(attributes.getString("typeHandlersPackage"))) {
            beanDefinitionBuilder.addPropertyValue("typeHandlersPackage", attributes.getString("typeHandlersPackage"));
        }
        if (StringUtils.hasText(attributes.getString("configLocation"))) {
            beanDefinitionBuilder.addPropertyValue("configLocation", attributes.getString("configLocation"));
        }
        if (StringUtils.hasText(attributes.getString("cache"))) {
            beanDefinitionBuilder.addPropertyReference("cache", attributes.getString("cache"));
        }
        if (StringUtils.hasText(attributes.getString("transactionFactory"))) {
            beanDefinitionBuilder.addPropertyReference("transactionFactory", attributes.getString("transactionFactory"));
        }
        if (StringUtils.hasText(attributes.getString("sqlSessionFactory"))) {
            beanDefinitionBuilder.addPropertyReference("sqlSessionFactory", attributes.getString("sqlSessionFactory"));
        }
        if (StringUtils.hasText(attributes.getString("typeAliasesPackage"))) {
            beanDefinitionBuilder.addPropertyValue("typeAliasesPackage", attributes.getString("typeAliasesPackage"));
        }
        beanDefinitionBuilder.addPropertyValue("failFast", attributes.getBoolean("failFast"));

        // SqlSessionFactoryBean 的 BeanDefinition
        BeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();

        String beanName = (String) attributes.get("value");
        registry.registerBeanDefinition(beanName, beanDefinition);

    }


    private Object resolvePlaceholder(Object value) {
        if (value instanceof String) {
            return environment.resolvePlaceholders((String) value);
        }
        return value;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
