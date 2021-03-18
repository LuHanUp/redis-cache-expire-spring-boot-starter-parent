package top.luhancc.redis.cache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author luHan
 * @create 2021/3/18 10:44
 * @since 1.0.0
 */
@SpringBootApplication
@EnableCaching
public class RedisCacheExpireApplication {
    public static void main(String[] args) {
        SpringApplication.run(RedisCacheExpireApplication.class, args);
    }
}
