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
package org.geektimes.spring.cache;

import org.geektimes.spring.mybatis.entry.Users;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * test EnableCaching
 *
 * @since 1.0.0
 * Date : 2021-05-06
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CacheableTest.class, CacheManagerConfig.class,TestService.class})
public class CacheableTest {

    @Autowired
    public TestService testService;


    @Test
    public void test() {
        System.out.println("---------------添加缓存------------------");
        Users user = testService.get("ty");
        System.out.println(user);
        System.out.println("---------------添加缓存------------------");
        Users user2 = testService.get("fzpb");
        System.out.println(user2);
        System.out.println("---------------获取缓存------------------");
        Users user3 = testService.get("ty");
        System.out.println(user3);
        System.out.println("---------------获取缓存------------------");
        Users user4 = testService.get("fzpb");
        System.out.println(user4);
        System.out.println("---------------删除缓存------------------");
        testService.eviect("ty");
        Users user5 = testService.get("ty");
        System.out.println(user5);
        Assert.assertNotEquals(user3,user5);
        System.out.println("---------------清除缓存------------------");
        testService.clear();
        Users user6 = testService.get("fzpb");
        System.out.println(user6);
        Assert.assertNotEquals(user4,user6);

    }


}
