package com.example.ratelimiter.controller;


import com.example.ratelimiter.service.RateLimiterService;
import com.example.ratelimiter.strategy.rateLimiterStrategies.FixedWindowRateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RateLimiterController {

    @Autowired
    private final RateLimiterService rateLimiterService;

    public RateLimiterController() {
        this.rateLimiterService = new RateLimiterService(new FixedWindowRateLimiter(5, 10000));
    }

    @GetMapping("/request/{userId}")
    public String request(@PathVariable String userId) {
        boolean allowed = rateLimiterService.isAllowed(userId);
        return allowed ? "Request allowed" : "Too many requests, try later!";
    }
}
