package top.luhancc.redis.cache.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import top.luhancc.redis.cache.core.ExpireRedisCacheManager;
import top.luhancc.redis.cache.core.RedisExpireCacheResolver;

/**
 * redis cache expire组件配置类
 *
 * @author luHan
 * @create 2021/3/18 10:03
 * @since 1.0.0
 */
public class RedisCacheExpireConfiguration extends CachingConfigurerSupport {
    private final RedisCacheConfiguration redisCacheConfiguration;
    private final RedisConnectionFactory redisConnectionFactory;

    @Override
    public CacheManager cacheManager() {
        return new ExpireRedisCacheManager(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory), redisCacheConfiguration);
    }

    @Bean(name = "redisExpireCacheResolver")
    @Override
    public CacheResolver cacheResolver() {
        RedisExpireCacheResolver redisExpireCacheResolver = new RedisExpireCacheResolver();
        redisExpireCacheResolver.setCacheManager(cacheManager());
        return redisExpireCacheResolver;
    }

    public RedisCacheExpireConfiguration(RedisConnectionFactory redisConnectionFactory, RedisCacheConfiguration redisCacheConfiguration) {
        this.redisConnectionFactory = redisConnectionFactory;
        this.redisCacheConfiguration = redisCacheConfiguration;
    }
}
