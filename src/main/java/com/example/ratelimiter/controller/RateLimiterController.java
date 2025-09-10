package com.example.ratelimiter.controller;

import com.example.ratelimiter.service.RateLimiterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RateLimiterController {

    private final RateLimiterService rateLimiterService;

    public RateLimiterController(RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    @GetMapping("/request/{userId}")
    public String request(@PathVariable String userId) {
        boolean allowed = rateLimiterService.isAllowed(userId);
        return allowed ? "Request allowed" : "Too many requests, try later!";
    }
    
    @GetMapping("/strategy")
    public String getCurrentStrategy() {
        return "Current rate limiting strategy: " + rateLimiterService.getCurrentStrategy();
    }
}
