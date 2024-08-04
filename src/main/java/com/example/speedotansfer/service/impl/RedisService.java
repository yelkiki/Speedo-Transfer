package com.example.speedotansfer.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RedisService {


    private final RedisTemplate<String, Long> redisTemplate;

    public void storeToken(String token, Long userId) {
        redisTemplate.opsForValue().set(token, userId);
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
