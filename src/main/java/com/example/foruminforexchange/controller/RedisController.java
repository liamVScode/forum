package com.example.foruminforexchange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping("/test-redis")
    public String testRedis() {
        redisTemplate.opsForValue().set("testKey", "Hello Redis");
        return redisTemplate.opsForValue().get("testKey");
    }
}
