package com.ums.schedule.adapter.cache;

import com.ums.schedule.common.util.JsonUtil;
import com.ums.schedule.domain.send.email.job.EmailSendJob;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class JobRepository {
    private final RedisTemplate<String, String> redisTemplate;

    public void store(EmailSendJob job) {
        String json = JsonUtil.toJson(job);
        double score = Instant.now().toEpochMilli();
        redisTemplate.opsForZSet().add("request:email", json, score);
    }
}
