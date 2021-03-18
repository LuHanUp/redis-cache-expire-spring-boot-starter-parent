package top.luhancc.redis.cache.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.support.NullValue;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.util.ByteUtils;
import org.springframework.util.ObjectUtils;
import top.luhancc.redis.cache.utils.RedisCacheConfigurationUtils;

import java.nio.ByteBuffer;
import java.util.concurrent.Callable;

/**
 * 对RedisCache进行包装
 * <p>支持设置过期时间</p>
 * <p>支持同name下不同key的过期时间不同</p>
 *
 * @author luHan
 * @create 2021/3/18 14:59
 * @since 1.0.0
 */
public class RedisCacheExpireWrapper extends RedisCache {
    private final Logger log = LoggerFactory.getLogger(RedisCacheExpireWrapper.class);
    private static final byte[] BINARY_NULL_VALUE = RedisSerializer.java().serialize(NullValue.INSTANCE);
    private final String name;
    private final RedisCache redisCache;
    private final RedisCacheWriter cacheWriter;
    private final RedisCacheConfiguration defaultCacheConfig;

    public RedisCacheExpireWrapper(String name, RedisCacheWriter cacheWriter,
                                   RedisCacheConfiguration defaultCacheConfig,
                                   RedisCache redisCache) {
        super(name, cacheWriter, defaultCacheConfig);
        this.name = name;
        this.redisCache = redisCache;
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfig = defaultCacheConfig;
    }

    @Override
    public String getName() {
        return redisCache.getName();
    }

    @Override
    public RedisCacheWriter getNativeCache() {
        return redisCache.getNativeCache();
    }

    @Override
    public ValueWrapper get(Object key) {
        ValueWrapper valueWrapper = redisCache.get(key);
        if (valueWrapper != null) {
            CacheOperationInvocationContextHolder.clear();
        }
        return valueWrapper;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        T t = redisCache.get(key, type);
        if (t != null) {
            CacheOperationInvocationContextHolder.clear();
        }
        return t;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        T t = redisCache.get(key, valueLoader);
        if (t != null) {
            CacheOperationInvocationContextHolder.clear();
        }
        return t;
    }

    @Override
    public void put(Object key, Object value) {
        Object cacheValue = preProcessCacheValue(value);
        if (!isAllowNullValues() && cacheValue == null) {
            throw new IllegalArgumentException(String.format(
                    "Cache '%s' does not allow 'null' values. Avoid storing null via '@Cacheable(unless=\"#result == null\")' or configure RedisCache to allow 'null' via RedisCacheConfiguration.",
                    name));
        }
        RedisCacheConfiguration redisCacheConfig = RedisCacheConfigurationUtils.expireRedisCacheConfig(defaultCacheConfig);
        // 设置过期时间
        if (redisCacheConfig != defaultCacheConfig) {
            log.info("key:[{}]自定了过期时间:[{}]", key, redisCacheConfig.getTtl());
        }
        cacheWriter.put(name, serializeCacheKey(createCacheKey(key)), serializeCacheValue(cacheValue), redisCacheConfig.getTtl());
        CacheOperationInvocationContextHolder.clear();
    }

    @Override
    public void evict(Object key) {
        redisCache.evict(key);
    }

    @Override
    public void clear() {
        redisCache.clear();
        CacheOperationInvocationContextHolder.clear();
    }

    @Override
    protected byte[] serializeCacheKey(String cacheKey) {
        return ByteUtils.getBytes(defaultCacheConfig.getKeySerializationPair().write(cacheKey));
    }

    @Override
    protected Object deserializeCacheValue(byte[] value) {
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfigurationUtils.expireRedisCacheConfig(defaultCacheConfig);
        if (isAllowNullValues() && ObjectUtils.nullSafeEquals(value, BINARY_NULL_VALUE)) {
            return NullValue.INSTANCE;
        }
        return cacheConfiguration.getValueSerializationPair().read(ByteBuffer.wrap(value));
    }
}
