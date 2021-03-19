package top.luhancc.redis.cache.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import top.luhancc.redis.cache.annotation.CacheExpire;

/**
 * @author luHan
 * @create 2021/3/19 10:05
 * @since 1.0.0
 */
@CacheExpire
@Cacheable(value = "cache-test2", key = "targetClass + methodName")
@Service
public class CacheService2 {

    public String data4() {
        return "this is cache data 2";
    }

    @CacheExpire(value = 10, ignore = true)
    public String data3() {
        return "this is cache data 3";
    }
}
