package com.ums.schedule.intergration.redis;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class RedisTest {
    @Autowired StringRedisTemplate redisTemplate;

    @Test
    public void should_save_and_get_value_from_redis() {
        // given
        String key = "id";
        String value = "jang314";
        //when
        redisTemplate.opsForValue().set(key, value);
        // then
        String getValue = redisTemplate.opsForValue().get(key);
        assertThat(getValue).isEqualTo(value);
    }

}
