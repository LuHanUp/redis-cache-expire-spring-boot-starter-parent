package top.luhancc.redis.cache.utils;

import org.springframework.data.redis.cache.RedisCacheConfiguration;
import top.luhancc.redis.cache.annotation.CacheExpire;
import top.luhancc.redis.cache.core.CacheOperationInvocationContextHolder;

import java.time.Duration;

/**
 * @author luHan
 * @create 2021/3/18 15:58
 * @since 1.0.0
 */
public final class RedisCacheConfigurationUtils {
    private RedisCacheConfigurationUtils() {
    }

    public static RedisCacheConfiguration newRedisCacheConfig(RedisCacheConfiguration defaultConfig, CacheExpire cacheExpire) {
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(cacheExpire.expire()))
                .serializeKeysWith(defaultConfig.getKeySerializationPair())
                .serializeValuesWith(defaultConfig.getValueSerializationPair());
        return cacheConfiguration;
    }

    public static RedisCacheConfiguration expireRedisCacheConfig(RedisCacheConfiguration defaultConfig) {
        CacheExpire cacheExpire = CacheOperationInvocationContextHolder.getCacheExpire();
        if (cacheExpire == null) {
            return defaultConfig;
        }
        return newRedisCacheConfig(defaultConfig, cacheExpire);
    }
}
