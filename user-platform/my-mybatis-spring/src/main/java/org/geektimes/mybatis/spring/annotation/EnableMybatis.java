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
package org.geektimes.mybatis.spring.annotation;

import org.apache.commons.configuration.Configuration;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 激活 MyBatis
 *
 * @since 1.0.0
 * Date : 2021-05-06
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
@MapperScan
@Import(MybatisBeanDefinitionRegistrar.class)
public @interface EnableMybatis {

    /**
     * @return the bean name of {@link SqlSessionFactoryBean}
     */
    String value() default "sqlSessionFactoryBean";

    @AliasFor(annotation = MapperScan.class)
    String[] basePackages() default {};

    /**
     * @return DataSource bean name
     */
    String dataSource() default "dataSource";

    /**
     * the location of {@link Configuration}
     *
     * @return
     */
    String configLocation() default "";


    /**
     * @return the location of {@link Mapper}
     * @see MapperScan
     */
    String[] mapperLocations() default {};

    String environment() default "SqlSessionFactoryBean";

    String typeAliasesPackage() default "";

    /**
     * typeHandlersPackage
     *
     * @return
     */
    String typeHandlersPackage() default "";

    /**
     * custom Cache  Bean name
     *
     * @return
     */
    String cache() default "";

    /**
     * custom transactionFactory bean name
     *
     * @return
     */
    String transactionFactory() default "";

    /**
     * custom SqlSessionFactory bean name
     *
     * @return
     */
    String sqlSessionFactory() default "";


    boolean failFast() default false;


}
