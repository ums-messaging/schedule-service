package com.ums.schedule.email.adaptor.in.queue.kafka;

import com.ums.schedule.common.code.request.SendGroupEventType;
import com.ums.schedule.email.adaptor.out.queue.kafka.SendGroupEventKafkaDispatcher;
import com.ums.schedule.email.application.event.SendTargetRequestEvent;
import com.ums.schedule.email.application.send.SendRequestUploadManager;
import com.ums.schedule.email.application.send.job.handler.SendMessageHandler;
import com.ums.schedule.email.application.send.manager.SendManager;
import com.ums.schedule.email.application.send.job.model.SendJob;
import com.ums.schedule.email.application.send.manager.SendTargetSendResultList;
import com.ums.schedule.email.application.send.mime.MessageContext;
import com.ums.schedule.email.domain.common.SendResult;
import com.ums.schedule.email.domain.event.SendGroupEventResult;
import com.ums.schedule.email.domain.event.SendTargetResultList;
import com.ums.schedule.email.domain.target.group.DomainGroup;
import com.ums.schedule.orchestrator.application.event.manager.handler.model.SendTargetRequestedEvent;
import com.ums.schedule.common.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Slf4j
@Component
public class SendTargetRequestKafkaListener {
    private final SendGroupEventKafkaDispatcher eventDispatcher;
    private final SendManager sendManager;
    private final SendRequestUploadManager uploadManager;
    private final ThreadPoolTaskExecutor executor;
    private final SendMessageHandler handler;

    public SendTargetRequestKafkaListener(SendMessageHandler handler,
                                          SendGroupEventKafkaDispatcher eventDispatcher,
                                          SendManager sendManager,
                                          SendRequestUploadManager uploadManager,
                                          @Qualifier("targetGroupUploadExecutor") ThreadPoolTaskExecutor executor) {
        this.handler = handler;
        this.eventDispatcher = eventDispatcher;
        this.sendManager = sendManager;
        this.uploadManager = uploadManager;
        this.executor = executor;
    }

    @KafkaListener(
            topics = "email-send-target-request",
            groupId = "email-target-upload-consumer",
            containerFactory = "emailSendListenerFactory",
            concurrency = "3"
    )
    public void listen(ConsumerRecord<String, String> record, Acknowledgment ack) {
        String payload = record.value();
        SendTargetRequestedEvent event = JsonUtil.toObject(payload, SendTargetRequestedEvent.class);
        SendJob job = event.job();
        SendResult sendResult = SendResult.of();
        DomainGroup group = DomainGroup.of(event.groupId(), event.groupKey());
        Path templateDir = null;
        try {
            templateDir = createMessageTempFile();
            MessageContext context = handler.executeHandler(templateDir, job, group);
            SendTargetResultList sendResultList =
                            sendManager.send(job.sendRequestId(), group, context.message(), context.targetList());// 멀티스레딩 처리하기
            uploadManager.upload(job, sendResult, sendResultList);
            ack.acknowledge();
        } catch (Exception e) {
            // EmailSendRequest의 상태를 ERROR로 변경
            sendResult = SendResult.of(e.getMessage());
            SendTargetResultList resultList = SendTargetResultList.of(job.requestId(), group, List.of());
//            uploadManager.upload(job, sendResult, resultList);
            log.error("error = ", e);
            ack.acknowledge();
        } finally {
            deleteFile(templateDir);

        }
    }

    private Path createMessageTempFile() throws IOException {
        return Files.createTempFile("template-", ".html");
    }
    private void deleteFile(Path path) {
        if(path != null) {
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                log.error("file.delete.error = ", e);
            }
        }
    }
    private void printKafkaLog(ConsumerRecord<String, String> record) {
        log.info(
                "Kafka consume - topic={}, partition={}, offset={}, key={}",
                record.topic(),
                record.partition(),
                record.offset(),
                record.key()
        );
    }
}
