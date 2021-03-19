package top.luhancc.redis.cache.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.luhancc.redis.cache.service.CacheService;
import top.luhancc.redis.cache.service.CacheService2;

/**
 * @author luHan
 * @create 2021/3/18 10:46
 * @since 1.0.0
 */
@RestController
@RequestMapping("/cache")
@RequiredArgsConstructor
public class CacheController {
    private final CacheService cacheService;
    private final CacheService2 cacheService2;

    @RequestMapping("/data")
    public String data() {
        return cacheService.data();
    }

    @RequestMapping("/data2")
    public String data2() {
        return cacheService.data2();
    }

    @RequestMapping("/data3")
    public String data3() {
        return cacheService2.data3();
    }

    @RequestMapping("/data4")
    public String data4() {
        return cacheService2.data4();
    }
}
