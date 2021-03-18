package top.luhancc.redis.cache.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import top.luhancc.redis.cache.annotation.CacheExpire;

/**
 * @author luHan
 * @create 2021/3/18 10:46
 * @since 1.0.0
 */
@Service
public class CacheService {

    @Cacheable(value = "cache-test", key = "targetClass + methodName")
    @CacheExpire(value = 100)
    public String data() {
        System.out.println("没走缓存,直接查询");
        return "hello this data is redis cache";
    }
}
