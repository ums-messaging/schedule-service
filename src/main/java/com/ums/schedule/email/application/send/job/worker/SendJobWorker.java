package com.ums.schedule.email.application.send.job.worker;

import com.ums.schedule.common.code.email.EmailResultCode;
import com.ums.schedule.common.code.request.SendGroupEventType;
import com.ums.schedule.email.adaptor.out.queue.kafka.SendGroupEventKafkaDispatcher;
import com.ums.schedule.email.application.event.SendGroupEventDispatcherFactory;
import com.ums.schedule.email.application.event.SendTargetRequestEvent;
import com.ums.schedule.email.application.target.SendTargetListUploadService;
import com.ums.schedule.email.application.send.job.model.SendJob;
import com.ums.schedule.email.common.code.SendTargetErrorTypeCode;
import com.ums.schedule.email.domain.event.DomainGroupEvent;
import com.ums.schedule.email.domain.event.SendGroupEventResult;
import com.ums.schedule.email.domain.event.SendTargetResultList;
import com.ums.schedule.email.domain.target.SendTargetError;
import com.ums.schedule.orchestrator.adaptor.out.queue.kafka.SendTargetRequestKafkaDispatcher;
import com.ums.schedule.email.application.target.reader.listener.model.SendTargetGroupedList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendJobWorker {
    private final SendTargetListUploadService uploader;
    private final SendTargetRequestKafkaDispatcher dispatcher;

    public Consumer<List<SendTargetGroupedList>> getSendTargetRequestWorker(SendJob job) {
        Consumer<List<SendTargetGroupedList>> consumer = rows -> {
                long startTime = System.currentTimeMillis();
                try {
                    List<CompletableFuture<SendTargetResultList>> futures = rows.stream()
                            .map(row -> uploader.upload(job.sendRequestId(), row))
                            .filter(v -> !v.successList().isEmpty())
                            .map(v -> dispatcher.publish(job, v.domainGroup(), v))
                            .toList();

                    CompletableFuture.allOf(
                            futures.stream().toArray(CompletableFuture[]::new)
                    ).join();

                    long endTime = System.currentTimeMillis();
                    log.info("futures.size = {}, duration.time = {}", futures.size(), endTime - startTime);
                } catch (Exception e) {
                    throw e;
                }
            };
        return consumer;
    }
}
