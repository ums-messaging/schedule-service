package com.ums.schedule.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisTest {
    private final StringRedisTemplate redisTemplate;

    public void test(String name) {
        redisTemplate.opsForValue().set("name", name);
        String value = redisTemplate.opsForValue().get("name");
        log.info("redis.test.value = {}", value);
    }

}
