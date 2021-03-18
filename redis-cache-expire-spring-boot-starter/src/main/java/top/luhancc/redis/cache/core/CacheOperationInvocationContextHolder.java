package top.luhancc.redis.cache.core;

import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import top.luhancc.redis.cache.annotation.CacheExpire;

/**
 * @author luHan
 * @create 2021/3/18 13:13
 * @since 1.0.0
 */
public class CacheOperationInvocationContextHolder {
    private static final ThreadLocal<CacheOperationInvocationContext<?>> CONTEXT_THREAD_LOCAL = new ThreadLocal<>();

    private static final ThreadLocal<CacheExpire> CACHE_EXPIRE_THREAD_LOCAL = new ThreadLocal<>();

    public static CacheOperationInvocationContext<?> context() {
        return CONTEXT_THREAD_LOCAL.get();
    }

    public static void setContext(CacheOperationInvocationContext<?> context) {
        CONTEXT_THREAD_LOCAL.set(context);
    }

    public static void clear() {
        CONTEXT_THREAD_LOCAL.remove();
        CACHE_EXPIRE_THREAD_LOCAL.remove();
    }

    public static void setCacheExpire(CacheExpire cacheExpire) {
        CACHE_EXPIRE_THREAD_LOCAL.set(cacheExpire);
    }

    public static CacheExpire getCacheExpire() {
        return CACHE_EXPIRE_THREAD_LOCAL.get();
    }

}
