package top.luhancc.redis.cache.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.luhancc.redis.cache.service.CacheService;

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

    @RequestMapping("/data")
    public String data() {
        return cacheService.data();
    }

    @RequestMapping("/data2")
    public String data2() {
        return cacheService.data2();
    }
}
