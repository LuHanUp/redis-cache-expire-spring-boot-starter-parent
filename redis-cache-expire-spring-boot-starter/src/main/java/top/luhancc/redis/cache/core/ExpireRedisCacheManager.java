package top.luhancc.redis.cache.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.lang.Nullable;

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
    private final RedisCacheWriter cacheWriter;

    private Map<String, Cache> singletonCacheObjects = new HashMap<>();

    public ExpireRedisCacheManager(RedisCacheWriter cacheWriter,
                                   RedisCacheConfiguration defaultCacheConfiguration) {
        super(cacheWriter, defaultCacheConfiguration);
        this.cacheWriter = cacheWriter;
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

    /**
     * 是否需要过期时间配置
     *
     * @return
     */
    private boolean needExpire() {
        return CacheOperationInvocationContextHolder.getCacheExpire() != null;
    }

    /**
     * 包装带过期时间的Cache
     *
     * @param cache
     * @return
     */
    private Cache wrapExpire(Cache cache) {
        if (cache instanceof RedisCache) {
            return wrapRedisCache(cache.getName(), defaultCacheConfiguration, (RedisCache) cache);
        }
        return cache;
    }

    /**
     * 对原始的RedisCache进行包装
     *
     * @param name                      cache name
     * @param defaultCacheConfiguration 默认的RedisCacheConfiguration
     * @param redisCache                原始RedisCache
     * @return
     */
    private RedisCache wrapRedisCache(String name, @Nullable RedisCacheConfiguration defaultCacheConfiguration, RedisCache redisCache) {
        return new RedisCacheExpireWrapper(name, cacheWriter, defaultCacheConfiguration, redisCache);
    }
}
