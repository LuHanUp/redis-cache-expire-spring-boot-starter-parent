package top.luhancc.redis.cache.core;

import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.SimpleCacheResolver;
import org.springframework.core.annotation.AnnotationUtils;
import top.luhancc.redis.cache.annotation.CacheExpire;

import java.util.Collection;

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
        CacheExpire cacheExpire = AnnotationUtils.getAnnotation(context.getMethod(), CacheExpire.class);
        if (cacheExpire == null) {
            cacheExpire = AnnotationUtils.findAnnotation(context.getMethod().getDeclaringClass(), CacheExpire.class);
        }
        if (cacheExpire != null && !cacheExpire.ignore()) {
            CacheOperationInvocationContextHolder.setCacheExpire(cacheExpire);
        }
        return super.resolveCaches(context);
    }
}
