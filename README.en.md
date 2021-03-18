# redis-cache-expire-spring-boot-starter-parent

#### Introduction
Use `redis` as the `spring cache` component, and you can **customize the expiration time**

1. 支持**方法级别**设置过期时间

#### Software Architecture
在`spring-boot-starter-data-redis`基础上改变了`RedisCacheManager`其`getCache`原始行为
在其基础上添加设置自定义过期时间的功能

#### Instructions for use
Because `redis-cache-expire-spring-boot-starter` does not integrate `spring-boot-starter-data-redis`, so need
Manually introduce dependencies
> Mainly to facilitate the use of the respective `spring-boot-starter-data-redis` version

1. Introduce `spring-boot-starter-data-redis` dependency
~~~xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
~~~
2. Introduce the starter `redis-cache-expire-spring-boot-starter` dependency
~~~xml
<dependency>
    <groupId>top.luhancc.redis</groupId>
    <artifactId>redis-cache-expire-spring-boot-starter</artifactId>
</dependency>
~~~
3. Just like using `spring cache`, use `@EnableCaching` to enable caching
~~~java
@SpringBootApplication
@EnableCaching
public class RedisCacheExpireApplication {
    public static void main(String[] args) {
        SpringApplication.run(RedisCacheExpireApplication.class, args);
    }
}
~~~

#### 参与贡献

1.  Fork the repository
2.  Create  feature/xxx branch
3.  Commit your code
4.  Create Pull Request
