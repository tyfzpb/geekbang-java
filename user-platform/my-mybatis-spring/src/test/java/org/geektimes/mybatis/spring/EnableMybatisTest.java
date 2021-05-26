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
package org.geektimes.mybatis.spring;

import org.geektimes.mybatis.spring.annotation.EnableMybatis;
import org.geektimes.mybatis.spring.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * test EnableMyBatis
 *
 * @since 1.0.0
 * Date : 2021-05-06
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {EnableMybatisTest.class, DatabaseConfig.class})
@EnableMybatis(dataSource = "demoDataSource",
        mapperLocations = "classpath*:mybatis/mappers/**/*.xml",
        typeAliasesPackage = "org.geektimes.mybatis.spring.entry.*",
        basePackages = "org.geektimes.**.mapper")
public class EnableMybatisTest {

    @Resource
    private UserMapper userMapper;


    @Test
    public void test() {
        System.out.println("---------------getAllUsers------------------");
        userMapper.getAllUsers().forEach(System.out::println);
    }


}
