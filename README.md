# redis-cache-expire-spring-boot-starter-parent

#### 介绍
使用`redis`作为`spring cache`组件，并且可以**自定义过期时间**

1. 支持**方法级别**设置过期时间

#### 软件架构
在`spring-boot-starter-data-redis`基础上改变了`RedisCacheManager`其`getCache`原始行为
在其基础上添加设置自定义过期时间的功能

#### 使用说明
因为`redis-cache-expire-spring-boot-starter`并没有集成`spring-boot-starter-data-redis`所以需要
手动引入依赖
> 主要是为了方便使用各自的`spring-boot-starter-data-redis`版本

1.  引入`spring-boot-starter-data-redis`依赖
~~~xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
~~~
2.  引入本starter依赖
~~~xml
<dependency>
    <groupId>top.luhancc.redis</groupId>
    <artifactId>redis-cache-expire-spring-boot-starter</artifactId>
</dependency>
~~~
3.  和使用`spring cache`一样 使用`@EnableCaching`开启缓存即可
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

1.  Fork 本仓库
2.  新建 feature/xxx 分支
3.  提交代码
4.  新建 Pull Request
