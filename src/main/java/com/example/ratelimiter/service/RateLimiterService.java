package com.example.ratelimiter.service;

import com.example.ratelimiter.strategy.rateLimiterStrategies.FixedWindowRateLimiter;
import org.springframework.stereotype.Service;

@Service
public class RateLimiterService {
    private final RateLimiter rateLimiter;

    public RateLimiterService(FixedWindowRateLimiter fixedWindowRateLimiter) {
        this.rateLimiter = new FixedWindowRateLimiter(5, 60000);
    }

    public boolean isAllowed(String userId) {
        return rateLimiter.allowRequest(userId);
    }
}
