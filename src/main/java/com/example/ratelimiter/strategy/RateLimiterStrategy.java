package com.example.ratelimiter.strategy;

/**
 * Strategy interface for different rate limiting algorithms
 */
public interface RateLimiterStrategy {
    /**
     * Check if a request from the given user should be allowed
     * @param userId the user identifier
     * @return true if request is allowed, false if rate limited
     */
    boolean allowRequest(String userId);
    
    /**
     * Get the name of this rate limiting strategy
     * @return strategy name
     */
    String getStrategyName();
} 