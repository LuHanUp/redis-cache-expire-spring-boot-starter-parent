package top.luhancc.redis.cache.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.util.ReflectionUtils;
import top.luhancc.redis.cache.annotation.CacheExpire;

import java.lang.reflect.Method;
import java.time.Duration;

/**
 * 实现自定义缓存过期时间的CacheManager
 *
 * @author luHan
 * @create 2021/3/18 10:06
 * @since 1.0.0
 */
public class ExpireRedisCacheManager extends RedisCacheManager {
    private final Logger log = LoggerFactory.getLogger(ExpireRedisCacheManager.class);
    private final RedisCacheConfiguration defaultCacheConfiguration;

    public ExpireRedisCacheManager(RedisCacheWriter cacheWriter,
                                   RedisCacheConfiguration defaultCacheConfiguration) {
        super(cacheWriter, defaultCacheConfiguration);
        this.defaultCacheConfiguration = defaultCacheConfiguration;
    }

    public Cache getCache(String name, CacheOperationInvocationContext<?> context) {
        Method targetMethod = ReflectionUtils.findMethod(context.getTarget().getClass(), context.getMethod().getName());
        Cache cache = getCache(name);
        if (targetMethod.isAnnotationPresent(CacheExpire.class)) {
            CacheExpire cacheExpire = AnnotationUtils.getAnnotation(targetMethod, CacheExpire.class);
            if (cache instanceof RedisCache) {
                RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofSeconds(cacheExpire.expire()))
                        .serializeKeysWith(defaultCacheConfiguration.getKeySerializationPair())
                        .serializeValuesWith(defaultCacheConfiguration.getValueSerializationPair());
                return createRedisCache(name, cacheConfiguration);
            }
        }
        return cache;
    }
}
