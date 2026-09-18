package com.ums.schedule.application.ums.common.send.email;

import com.ums.schedule.adapter.persistence.TargetGroupQueryResult;
import com.ums.schedule.common.code.email.EmailResultCode;
import com.ums.schedule.common.code.request.SendGroupEventType;
import com.ums.schedule.common.exception.BusinessException;
import com.ums.schedule.common.util.JsonUtil;
import com.ums.schedule.domain.send.email.job.DomainGroup;
import com.ums.schedule.adapter.persistence.TargetMessageQueryRepository;
import com.ums.schedule.domain.send.group.SendGroupEvent;
import com.ums.schedule.domain.send.group.SendGroupEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.internals.Acknowledgements;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailJobWorker {
    private final SendGroupEventRepository eventRepository;
    private final TargetMessageQueryRepository repository;

    @KafkaListener(
            topics = "email-send-request",
            groupId = "email-send-consumer",
            containerFactory = "emailSendListenerFactory"
    )
    public void work(String payload, Acknowledgment ack) {
        DomainGroup group = JsonUtil.toObject(payload, DomainGroup.class);
        try {
            long startMs = System.currentTimeMillis();
            List<TargetGroupQueryResult> targetList
                    = repository.findGroupByGroupIdAndDomain(group.uploadId(), group.groupId(), group.groupKey());
            log.info("select.query.duration.time = {}", System.currentTimeMillis()-startMs);
            log.info("[{}][{}] group.result = {}", Thread.currentThread().getId(), group.requestId(), group.groupId());
            for (TargetGroupQueryResult result : targetList) {
            }
            eventRepository.saveAndFlush(SendGroupEvent.of(SendGroupEventType.SEND_COMPLETED, group, EmailResultCode.SUCCESS, null));
            ack.acknowledge();
        } catch (DataIntegrityViolationException e) {
            log.error("error = {}", e.getMessage());
            ack.acknowledge();
        } catch (Exception e){
            // 영구에러
            log.error("permanant.error = {}", e.getMessage());
            eventRepository.saveAndFlush(SendGroupEvent.of(SendGroupEventType.SEND_COMPLETED, group, EmailResultCode.FAIL, e.getMessage()));
            ack.acknowledge();
        }
    }
}
