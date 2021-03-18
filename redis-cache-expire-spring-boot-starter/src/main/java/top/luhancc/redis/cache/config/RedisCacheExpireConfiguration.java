package top.luhancc.redis.cache.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import top.luhancc.redis.cache.core.ExpireRedisCacheManager;
import top.luhancc.redis.cache.core.RedisExpireCacheResolver;

import java.lang.reflect.Field;

/**
 * redis cache expire组件配置类
 *
 * @author luHan
 * @create 2021/3/18 10:03
 * @since 1.0.0
 */
public class RedisCacheExpireConfiguration extends CachingConfigurerSupport {
    private final RedisCacheManager redisCacheManager;
    private static volatile ExpireRedisCacheManager redisCacheManagerWrapper;

    @Override
    public CacheManager cacheManager() {
        if (redisCacheManagerWrapper == null) {
            synchronized (RedisCacheExpireConfiguration.class) {
                if (redisCacheManagerWrapper == null) {
                    redisCacheManagerWrapper = new ExpireRedisCacheManager(redisCacheWriter(), redisCacheConfiguration());
                }
            }
        }
        return redisCacheManagerWrapper;
    }

    @Bean(name = "redisExpireCacheResolver")
    @Override
    public CacheResolver cacheResolver() {
        RedisExpireCacheResolver redisExpireCacheResolver = new RedisExpireCacheResolver();
        redisExpireCacheResolver.setCacheManager(cacheManager());
        return redisExpireCacheResolver;
    }

    public RedisCacheExpireConfiguration(RedisCacheManager redisCacheManager) {
        this.redisCacheManager = redisCacheManager;
    }

    private RedisCacheWriter redisCacheWriter() {
        try {
            Field cacheWriterField = redisCacheManager.getClass().getDeclaredField("cacheWriter");
            cacheWriterField.setAccessible(true);
            return (RedisCacheWriter) cacheWriterField.get(redisCacheManager);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private RedisCacheConfiguration redisCacheConfiguration() {
        try {
            Field defaultCacheConfigField = redisCacheManager.getClass().getDeclaredField("defaultCacheConfig");
            defaultCacheConfigField.setAccessible(true);
            return (RedisCacheConfiguration) defaultCacheConfigField.get(redisCacheManager);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
