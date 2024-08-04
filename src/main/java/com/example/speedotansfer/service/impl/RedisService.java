package com.example.speedotansfer.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;


@Service
@RequiredArgsConstructor
public class RedisService {


    private final RedisTemplate<String, Long> redisTemplate;

    public void storeToken(String token, Long userId, long expirationTimeInSeconds) {
        redisTemplate.opsForValue().set(token, userId, Duration.ofSeconds(expirationTimeInSeconds));
    }

    public Long getUserIdByToken(String token) {
        return redisTemplate.opsForValue().get(token);
    }

    public boolean exists(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(token));
    }

    public void deleteToken(String token) {
        redisTemplate.delete(token);
    }
}