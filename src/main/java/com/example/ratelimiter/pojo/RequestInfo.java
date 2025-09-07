package com.example.ratelimiter.pojo;

public class RequestInfo {
    public int requestCount;
    public Long windowStartTime;

    public RequestInfo(int requestCount, Long windowStartTime) {
        this.requestCount = requestCount;
        this.windowStartTime = windowStartTime;
    }
}
