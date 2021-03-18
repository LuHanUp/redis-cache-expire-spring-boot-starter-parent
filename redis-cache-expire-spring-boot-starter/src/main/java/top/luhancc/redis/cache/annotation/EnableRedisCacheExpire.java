package top.luhancc.redis.cache.annotation;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import top.luhancc.redis.cache.config.RedisCacheExpireConfiguration;

import java.lang.annotation.*;

/**
 * 开启redis cache自定义过期时间组件
 *
 * @author luHan
 * @create 2021/3/18 09:42
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@ImportAutoConfiguration(classes = {RedisCacheExpireConfiguration.class})
public @interface EnableRedisCacheExpire {
}
