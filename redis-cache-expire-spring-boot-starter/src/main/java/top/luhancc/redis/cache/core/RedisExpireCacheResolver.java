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
        CacheOperationInvocationContextHolder.setContext(context);
        Collection<? extends Cache> caches = super.resolveCaches(context);
        CacheOperationInvocationContextHolder.clear();
        return caches;
    }
}
