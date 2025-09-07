package com.example.ratelimiter.service;

public interface RateLimiter {
    boolean allowRequest(String userId);
}
