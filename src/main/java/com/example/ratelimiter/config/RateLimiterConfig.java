package com.example.ratelimiter.config;

import com.example.ratelimiter.strategy.RateLimiterStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RateLimiterConfig {

    @Value("${ratelimiter.strategy:inMemory}")
    private String strategyName;

    @Bean
    @Primary
    public RateLimiterStrategy rateLimiterStrategy(
            @Qualifier("fixedWindow") RateLimiterStrategy fixedWindow,
            @Qualifier("inMemory") RateLimiterStrategy inMemory) {
        
        // Select strategy based on configuration
        switch (strategyName.toLowerCase()) {
            case "fixedwindow":
            case "fixed":
                return fixedWindow;
            case "inmemory":
            case "memory":
            default:
                return inMemory;
        }
    }
}
