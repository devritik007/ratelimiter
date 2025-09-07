package com.example.ratelimiter.strategy.rateLimiterStrategies;

import com.example.ratelimiter.pojo.RequestInfo;
import com.example.ratelimiter.service.RateLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class FixedWindowRateLimiter implements RateLimiter {
    private final int maxRequests;
    private final long windowSizeMillis;
    private final Map<String, RequestInfo> userRequestMap = new HashMap<>();

    public FixedWindowRateLimiter(@Value("${ratelimiter.fixed.limit:10}") int maxRequest, @Value("${ratelimiter.fixed.window:1000}") int windowSizeMilli) {
        this.maxRequests = maxRequest;
        this.windowSizeMillis = windowSizeMilli;
    }

    // synchronized = Methods can only be accessed by one thread at a time
    @Override
    public synchronized boolean allowRequest(String userId) {
        Long currentTime = System.currentTimeMillis();
        RequestInfo requestInfo = userRequestMap.getOrDefault(userId, new RequestInfo(0, currentTime));

        if(currentTime- requestInfo.windowStartTime > windowSizeMillis) {
            requestInfo.requestCount = 1;
            requestInfo.windowStartTime = currentTime;
            userRequestMap.put(userId, requestInfo);
            return true;
        }
        if(requestInfo.requestCount < maxRequests) {
            requestInfo.requestCount++;
            requestInfo.windowStartTime = currentTime;
            userRequestMap.put(userId, requestInfo);
            return true;
        }
        return false;
    }
}
