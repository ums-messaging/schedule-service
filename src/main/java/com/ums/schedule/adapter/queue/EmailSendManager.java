package com.ums.schedule.adapter.queue;

import com.ums.schedule.adapter.api.email.smtp.EmailSmtpClient;
import com.ums.schedule.domain.send.email.job.DomainGroup;
import com.ums.schedule.domain.send.email.job.DomainGroupTarget;
import com.ums.schedule.domain.send.email.job.EmailSendJob;
import com.ums.schedule.common.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EmailSendManager {
    private final EmailSmtpClient smtpClient;
    private final RedisTemplate<String, String> redisTemplate;

    @KafkaListener(topics = "email_send_request")
    public void consume(String payload) {
    EmailSendJob job = JsonUtil.toObject(payload, EmailSendJob.class);
    Set<String> domainGroup = redisTemplate.opsForZSet().rangeByScore("request:email", job.job().jobId(), job.job().jobId());

    List<List<DomainGroupTarget>> groups = domainGroup.stream()
                    .map(group -> JsonUtil.toList(group, DomainGroupTarget.class)
                    ).toList();

        for (List<DomainGroupTarget> targetGroup : groups) {
            Map<String, List<DomainGroup>> groupList = targetGroup.stream()
                    .map(target -> target.group())
                    .collect(Collectors.groupingBy(DomainGroup::domain));

            CompletableFuture[] futures = groupList.entrySet()
                    .stream()
                    .map(entry -> CompletableFuture
                                        .supplyAsync(() -> smtpClient.send(job, entry.getKey(), targetGroup))
                    )
                    .toArray(CompletableFuture[]::new);

            CompletableFuture.allOf(futures);
        }
    }
}
