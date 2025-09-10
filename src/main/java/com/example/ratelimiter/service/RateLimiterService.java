package com.example.ratelimiter.service;

import com.example.ratelimiter.strategy.RateLimiterStrategy;
import org.springframework.stereotype.Service;

@Service
public class RateLimiterService {
    private final RateLimiterStrategy rateLimiterStrategy;

    public RateLimiterService(RateLimiterStrategy rateLimiterStrategy) {
        this.rateLimiterStrategy = rateLimiterStrategy;
    }

    public boolean isAllowed(String userId) {
        return rateLimiterStrategy.allowRequest(userId);
    }
    
    public String getCurrentStrategy() {
        return rateLimiterStrategy.getStrategyName();
    }
}
