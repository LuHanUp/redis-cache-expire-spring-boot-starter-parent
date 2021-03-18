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
import java.util.HashMap;
import java.util.Map;

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

    private Map<String, Cache> singletonCacheObjects = new HashMap<>();

    public ExpireRedisCacheManager(RedisCacheWriter cacheWriter,
                                   RedisCacheConfiguration defaultCacheConfiguration) {
        super(cacheWriter, defaultCacheConfiguration);
        this.defaultCacheConfiguration = defaultCacheConfiguration;
    }

    @Override
    public Cache getCache(String name) {
        if (!singletonCacheObjects.containsKey(name)) {
            Cache cache = super.getCache(name);

            // 如果需要过期时间  就将cache进行包装下
            if (needExpire()) {
                cache = wrapExpire(cache);
            }
            singletonCacheObjects.put(name, cache);
            return cache;
        }
        return singletonCacheObjects.get(name);
    }

    private boolean needExpire() {
        CacheOperationInvocationContext<?> context = CacheOperationInvocationContextHolder.context();
        Method targetMethod = ReflectionUtils.findMethod(context.getTarget().getClass(), context.getMethod().getName());
        return targetMethod != null && targetMethod.isAnnotationPresent(CacheExpire.class);
    }

    private Cache wrapExpire(Cache cache) {
        CacheOperationInvocationContext<?> context = CacheOperationInvocationContextHolder.context();
        Method targetMethod = ReflectionUtils.findMethod(context.getTarget().getClass(), context.getMethod().getName());
        if (targetMethod == null) {
            return cache;
        }
        CacheExpire cacheExpire = AnnotationUtils.getAnnotation(targetMethod, CacheExpire.class);
        if (cache instanceof RedisCache) {
            RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                    .entryTtl(Duration.ofSeconds(cacheExpire.expire()))
                    .serializeKeysWith(defaultCacheConfiguration.getKeySerializationPair())
                    .serializeValuesWith(defaultCacheConfiguration.getValueSerializationPair());
            log.info("对{}进行过期时间[{}]包装", cache, cacheConfiguration.getTtl());
            return createRedisCache(cache.getName(), cacheConfiguration);
        }
        return cache;
    }
}
