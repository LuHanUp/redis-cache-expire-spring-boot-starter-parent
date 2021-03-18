package top.luhancc.redis.cache.core;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.SimpleCacheResolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * 自定义过期时间的CacheResolver
 *
 * @author luHan
 * @create 2021/2/18 13:09
 * @since 1.0.0
 */
public class RedisExpireCacheResolver extends SimpleCacheResolver {
    @Override
    public Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> context) {
        Collection<String> cacheNames = getCacheNames(context);
        if (cacheNames == null) {
            return Collections.emptyList();
        }
        Collection<Cache> result = new ArrayList<>(cacheNames.size());
        for (String cacheName : cacheNames) {
            CacheManager cacheManager = getCacheManager();
            Cache cache;
            if (cacheManager instanceof ExpireRedisCacheManager) {
                ExpireRedisCacheManager expireRedisCacheManager = (ExpireRedisCacheManager) cacheManager;
                cache = expireRedisCacheManager.getCache(cacheName, context);
            } else {
                cache = cacheManager.getCache(cacheName);
            }
            if (cache == null) {
                throw new IllegalArgumentException("Cannot find cache named '" +
                        cacheName + "' for " + context.getOperation());
            }
            result.add(cache);
        }
        return result;
    }
}
