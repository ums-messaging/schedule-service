package com.ums.schedule.intergration.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ums.schedule.adapter.api.email.dns.DnsQueryClient;
import com.ums.schedule.adapter.api.email.dns.DnsQueryResult;
import com.ums.schedule.application.send.mime.MimeHtmlWriter;
import com.ums.schedule.application.send.mime.MimeWriter;
import com.ums.schedule.adapter.storage.AwsS3Repository;
import com.ums.schedule.domain.send.email.job.DomainGroupTarget;
import com.ums.schedule.domain.send.email.job.EmailSendJob;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

@Component
@Profile("test")
public class KafkaTestConsumer {
    private AwsS3Repository repository;
    private DnsQueryClient client;
    private static final String TEST_SCHEDULE_TOPIC = "email-send";
    private static final String TEST_SCHEDULE_GROUP = "email-send-group";
    private ObjectMapper mapper;
    private RedisTemplate<String, String> redisTemplate;

    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();
    @KafkaListener(topics = TEST_SCHEDULE_TOPIC, groupId = TEST_SCHEDULE_GROUP)
    public void consume(String message) throws JsonProcessingException {
        EmailSendJob job = mapper.readValue(message, EmailSendJob.class);
        Set<String> payloads = redisTemplate.opsForZSet().rangeByScore("request:email", job.job().jobId(), job.job().jobId());
        // 멀티스레드 처리
        for (String payload : payloads) {
            List<DomainGroupTarget> targetList = mapper.readValue(payload, new TypeReference<List<DomainGroupTarget>>() {
            });
            MimeWriter writer = new MimeHtmlWriter();
            // dns 질의
            for (DomainGroupTarget target : targetList) {
                String domain = target.getDomain();
                DnsQueryResult domainInfo = client.getDomainInfo(domain);
//                MimeMessage mimeMessage = MimeMessage.of(job.imageDir(), target);
//                EmailSmtpClient[] client = new EmailSmtpClient[10];


//                smtpHelper.getSession(); // 세션 정보 가져오기
//                smtpAgent.send(job, target, mimeMessage); // 발송
            }
        }

        try {
//            queue.offer(message);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public String awaitMessage(long timeoutMs) throws InterruptedException {
//            return queue.poll(timeoutMs, TimeUnit.SECONDS);
//    }
}
