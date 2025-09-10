package com.example.ratelimiter.strategy.impl;

import com.example.ratelimiter.strategy.RateLimiterStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-Memory Rate Limiter Implementation
 * Uses local memory for rate limiting - good for testing and single instance deployments
 */
@Component("inMemory")
public class InMemoryRateLimiter implements RateLimiterStrategy {

    @Value("${ratelimiter.memory.limit:5}")
    private int limit;
    
    @Value("${ratelimiter.memory.window:60000}")
    private long windowMillis;

    private final ConcurrentHashMap<String, UserRequestInfo> userRequests = new ConcurrentHashMap<>();

    @Override
    public boolean allowRequest(String userId) {
        long currentTime = System.currentTimeMillis();
        
        userRequests.compute(userId, (key, requestInfo) -> {
            if (requestInfo == null) {
                return new UserRequestInfo(1, currentTime);
            }
            
            if (currentTime - requestInfo.windowStart.get() >= windowMillis) {
                requestInfo.count.set(1);
                requestInfo.windowStart.set(currentTime);
                return requestInfo;
            }
            
            requestInfo.count.incrementAndGet();
            return requestInfo;
        });
        
        UserRequestInfo requestInfo = userRequests.get(userId);
        return requestInfo != null && requestInfo.count.get() <= limit;
    }
    
    @Override
    public String getStrategyName() {
        return "In-Memory Fixed Window";
    }
    
    private static class UserRequestInfo {
        final AtomicInteger count;
        final AtomicLong windowStart;
        
        UserRequestInfo(int initialCount, long windowStart) {
            this.count = new AtomicInteger(initialCount);
            this.windowStart = new AtomicLong(windowStart);
        }
    }
}