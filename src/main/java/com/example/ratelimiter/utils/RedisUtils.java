package com.example.ratelimiter.utils;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

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

    public Long eval(String script, List<String> keys, List<String> args) {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(script);
        redisScript.setResultType(Long.class);
        return redisTemplate.execute(redisScript, keys, args.toArray());
    }
}
