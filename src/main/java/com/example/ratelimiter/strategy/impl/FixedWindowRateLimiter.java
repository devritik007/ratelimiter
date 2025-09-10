package com.example.ratelimiter.strategy.impl;

import com.example.ratelimiter.strategy.RateLimiterStrategy;
import com.example.ratelimiter.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Fixed Window Rate Limiter Implementation
 * Allows a fixed number of requests per time window
 */
@Component("fixedWindow")
public class FixedWindowRateLimiter implements RateLimiterStrategy {

    private final RedisUtils redisUtils;
    
    @Value("${ratelimiter.fixed.limit:5}")
    private int limit;
    
    @Value("${ratelimiter.fixed.window:60000}")
    private long windowMillis;

    public FixedWindowRateLimiter(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    @Override
    public boolean allowRequest(String userId) {
        String key = "fixed_window:" + userId;
        
        Long counter = redisUtils.increament(key);
        
        if (counter != null && counter == 1) {
            redisUtils.expire(key, windowMillis);
        }
        return counter != null && counter <= limit;
    }
    
    @Override
    public String getStrategyName() {
        return "Fixed Window";
    }
} 