package com.ums.schedule.adapter.queue;

import com.ums.schedule.adapter.api.email.smtp.EmailSmtpClient;
import com.ums.schedule.domain.send.email.job.DomainGroup;
import com.ums.schedule.domain.send.email.job.DomainGroupTarget;
import com.ums.schedule.domain.send.email.job.EmailSendJob;
import com.ums.schedule.common.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EmailSendManager {
    private final EmailSmtpClient smtpClient;

    public void consume(String payload) {

    }
}
