package org.geektimes.spring.cache;

import org.geektimes.spring.mybatis.entry.Users;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    @Cacheable(value = {"users"}, key = "#name", cacheManager = "cacheManager")
    public Users get(String name) {
        System.out.println("设置缓存：" + name);
        return new Users(System.currentTimeMillis(), name);
    }

    @CacheEvict(value = {"users"}, key = "#name", cacheManager = "cacheManager")
    public void eviect(String name) {
        System.out.println("删除缓存：" + name);
    }

    @CacheEvict(value = {"users"}, allEntries = true, cacheManager = "cacheManager")
    public void clear() {
        System.out.println("清空缓存");
    }
}
