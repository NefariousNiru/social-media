package com.nefarious.socialnetwork.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RateLimiterService {
    private final StringRedisTemplate redis;
    private final Environment env;

    private static final Duration WINDOW = Duration.ofMinutes(15);

    public boolean tryAcquire(String key, String propertyName) {
        int limit = Integer.parseInt(env.getProperty(propertyName, "3")); // default fallback to 3
        String redisKey = "rate-limit:" + key;
        Long attempts = redis.opsForValue().increment(redisKey);
        if (attempts == 1) {
            redis.expire(redisKey, WINDOW);
        }
        return attempts <= limit;
    }

    public void resetLimit(String key) {
        redis.delete("rate-limit:" + key);
    }
}
