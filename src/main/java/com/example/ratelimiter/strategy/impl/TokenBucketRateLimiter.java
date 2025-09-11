package com.example.ratelimiter.strategy.impl;

import com.example.ratelimiter.strategy.RateLimiterStrategy;
import com.example.ratelimiter.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component("tokenBucket")
public class TokenBucketRateLimiter implements RateLimiterStrategy {

    private final RedisUtils redisUtils;

    @Value("${ratelimiter.token.capacity:5}")
    private int capacity;

    @Value("${ratelimiter.token.refillRate:1}")
    private int refillRate;

    private static final String LUA_SCRIPT =
            "local tokens_key = KEYS[1] " +
            "local timestamp_key = KEYS[2] " +
            "local capacity = tonumber(ARGV[1]) " +
            "local refill_rate = tonumber(ARGV[2]) " +
            "local now = tonumber(ARGV[3]) " +
            "local requested = tonumber(ARGV[4]) " +

            "local last_refill = tonumber(redis.call('get', timestamp_key) or 0) " +
            "local tokens = tonumber(redis.call('get', tokens_key) or capacity) " +

            "local delta = math.max(0, now - last_refill) " +
            "local refill = delta * refill_rate " +
            "tokens = math.min(capacity, tokens + refill) " +

            "if tokens >= requested then " +
            "    tokens = tokens - requested " +
            "    redis.call('set', tokens_key, tokens) " +
            "    redis.call('set', timestamp_key, now) " +
            "    return 1 " +
            "else " +
            "    redis.call('set', tokens_key, tokens) " +
            "    redis.call('set', timestamp_key, now) " +
            "    return 0 " +
            "end";

    public TokenBucketRateLimiter(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    @Override
    public boolean allowRequest(String userId) {
        String tokensKey = "rate_limiter:tokens:" + userId;
        String timestampKey = "rate_limiter:timestamp:" + userId;

        Long result = redisUtils.eval(
                LUA_SCRIPT,
                Arrays.asList(tokensKey, timestampKey),
                Arrays.asList(
                        String.valueOf(capacity),
                        String.valueOf(refillRate),
                        String.valueOf(System.currentTimeMillis() / 1000),
                        "1"
                )
        );

        return result != null && result == 1;
    }

    @Override
    public String getStrategyName() {
        return "Token Bucket";
    }
}
