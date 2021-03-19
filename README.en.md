# redis-cache-expire-spring-boot-starter-parent

#### Introduction
Use `redis` as the `spring cache` component, and you can **customize the expiration time**

1. Support **method level** to set expiration time
2. Support adding annotations to the class, the general expiration time of the cache method under this class
3. Support ignore function

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

#### example
**method level**
~~~java
@Service
public class CacheService {
    @Cacheable(value = "cache-test", key = "targetClass + methodName")
    @CacheExpire(value = 100)
    public String data() {
        System.out.println("没走缓存,直接查询");
        return "hello this data is redis cache";
    }
    @Cacheable(value = "cache-test", key = "targetClass + methodName")
    public String data2() {
        System.out.println("没走缓存,直接查询");
        return "hello this data is redis cache is not expire";
    }
}
~~~
**class level**
~~~java
@CacheExpire
@Cacheable(value = "cache-test2", key = "targetClass + methodName")
@Service
public class CacheService2 {
    public String data4() {
        return "this is cache data 2";
    }
    @CacheExpire(value = 10, ignore = true)
    public String data3() {
        return "this is cache data 3";
    }
}
~~~

#### 参与贡献

1.  Fork the repository
2.  Create  feature/xxx branch
3.  Commit your code
4.  Create Pull Request
