package top.luhancc.redis.cache.config;

import org.springframework.beans.BeansException;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.CacheOperationSource;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import top.luhancc.redis.cache.core.RedisExpireCacheManager;
import top.luhancc.redis.cache.core.RedisExpireCacheResolver;

import java.lang.reflect.Field;

/**
 * redis cache expire组件配置类
 *
 * @author luHan
 * @create 2021/3/18 10:03
 * @since 1.0.0
 */
public class RedisCacheExpireConfiguration extends CachingConfigurerSupport implements ApplicationContextAware {
    private final RedisCacheManager redisCacheManager;
    private final RedisConnectionFactory redisConnectionFactory;
    private static volatile RedisExpireCacheManager redisCacheManagerWrapper;

    private ApplicationContext applicationContext;

    public RedisCacheExpireConfiguration(RedisCacheManager redisCacheManager,
                                         RedisConnectionFactory redisConnectionFactory,
                                         CacheOperationSource cacheOperationSource) {
        this.redisCacheManager = redisCacheManager;
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @Override
    public CacheManager cacheManager() {
        if (redisCacheManagerWrapper == null) {
            synchronized (RedisCacheExpireConfiguration.class) {
                if (redisCacheManagerWrapper == null) {
                    redisCacheManagerWrapper = new RedisExpireCacheManager(redisCacheWriter(), redisCacheConfiguration());
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

    private RedisCacheWriter redisCacheWriter() {
        try {
            Field cacheWriterField = redisCacheManager.getClass().getDeclaredField("cacheWriter");
            cacheWriterField.setAccessible(true);
            return (RedisCacheWriter) cacheWriterField.get(redisCacheManager);
        } catch (Exception e) {
            // 获取cacheWriter失败
        }
        // 尝试从容器中获取cacheWriter
        RedisCacheWriter cacheWriter;
        try {
            cacheWriter = applicationContext.getBean(RedisCacheWriter.class);
        } catch (BeansException e) {
            // 表示从spring容器中也获取不到cacheWriter
            cacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
        }
        return cacheWriter;
    }

    private RedisCacheConfiguration redisCacheConfiguration() {
        try {
            Field defaultCacheConfigField = redisCacheManager.getClass().getDeclaredField("defaultCacheConfig");
            defaultCacheConfigField.setAccessible(true);
            return (RedisCacheConfiguration) defaultCacheConfigField.get(redisCacheManager);
        } catch (Exception e) {
            // 获取defaultCacheConfig失败
        }
        // 尝试从容器中获取bean
        RedisCacheConfiguration defaultCacheConfig = null;
        try {
            defaultCacheConfig = applicationContext.getBean(RedisCacheConfiguration.class);
        } catch (BeansException e) {
            // 表示从spring容器也获取不到  直接创建一个默认的
            defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig();
        }
        return defaultCacheConfig;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
