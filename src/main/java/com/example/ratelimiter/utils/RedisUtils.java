package com.example.ratelimiter.utils;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RedisUtils {
    private final StringRedisTemplate redisTemplate;

    public RedisUtils(StringRedisTemplate stringRedisTemplate) {
        this.redisTemplate = stringRedisTemplate;
    }

    public Long increament(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    public void expire(String key, Long millis) {
        redisTemplate.expire(key, Duration.ofMillis(millis));
    }
}
