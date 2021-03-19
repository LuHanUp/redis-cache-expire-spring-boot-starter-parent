package top.luhancc.redis.cache.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 自定义方法缓存的过期时间注解
 *
 * @author luHan
 * @create 2021/2/18 11:35
 * @since 1.0.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CacheExpire {
    /**
     * expire time, default -1  单位 s
     */
    @AliasFor("expire")
    long value() default -1;

    /**
     * expire time, default -1 单位 s
     */
    @AliasFor("value")
    long expire() default -1;

    /**
     * 是否忽略 当设置为true时 此注解设置的任何操作都不会生效
     *
     * @return
     */
    boolean ignore() default false;
}
