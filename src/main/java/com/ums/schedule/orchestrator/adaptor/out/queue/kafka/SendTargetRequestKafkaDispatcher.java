package com.ums.schedule.orchestrator.adaptor.out.queue.kafka;

import com.ums.schedule.common.code.request.SendGroupEventType;
import com.ums.schedule.email.adaptor.out.queue.kafka.SendGroupEventKafkaDispatcher;
import com.ums.schedule.email.application.send.job.model.SendJob;
import com.ums.schedule.email.common.code.SendTargetErrorTypeCode;
import com.ums.schedule.email.domain.event.SendGroupEventResult;
import com.ums.schedule.email.domain.event.SendTargetResultList;
import com.ums.schedule.email.domain.target.SendTargetError;
import com.ums.schedule.email.domain.target.group.DomainGroup;
import com.ums.schedule.orchestrator.application.event.manager.handler.model.SendTargetRequestedEvent;
import com.ums.schedule.orchestrator.adaptor.out.queue.SendTargetRequestDispatcher;

import com.ums.schedule.common.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@Component
@Slf4j
public class SendTargetRequestKafkaDispatcher implements SendTargetRequestDispatcher {
    private final SendGroupEventKafkaDispatcher dispatcher;
    private final KafkaTemplate<String, Object> template;

    public SendTargetRequestKafkaDispatcher(SendGroupEventKafkaDispatcher dispatcher,
                                        @Qualifier("targetGroupUploadTemplate")
                                                    KafkaTemplate<String, Object> template) {
        this.dispatcher = dispatcher;
        this.template = template;
    }

    @Override
    public CompletableFuture<SendTargetResultList> publish(SendJob job, DomainGroup group, SendTargetResultList resultList) {
        long startMs = System.currentTimeMillis();
        SendTargetRequestedEvent event = SendTargetRequestedEvent.of(job, group, resultList.successList().size());
        return send(resultList, startMs, event)
                .thenApply(v -> resultList)
                .exceptionally(e -> {
                    List<SendTargetError> errorList = getErrorList(resultList, e.getMessage());
                    SendTargetResultList list = SendTargetResultList.of(job.sendRequestId(), group, List.of(), errorList);
                    SendGroupEventResult eventResult = SendGroupEventResult.of();
                    dispatcher.publish(SendGroupEventType.SEND_REQUESTED, eventResult, list);
                    return list;
                });
    }

    private List<SendTargetError> getErrorList(SendTargetResultList resultList, String errorMessage) {
        return Stream.concat(resultList.errorList().stream(),
                resultList.successList()
                        .stream()
                        .map(target -> SendTargetError.of(target, SendTargetErrorTypeCode.TARGET_REQUEST_ERROR, errorMessage))
        ).toList();
    }

    private CompletableFuture<SendResult<String, Object>> send(SendTargetResultList resultList, long startMs, SendTargetRequestedEvent event) {
        CompletableFuture<SendResult<String, Object>> future =
                template.send("email-send-target-request",
                        String.valueOf(resultList.domainGroup().groupId()),
                        JsonUtil.toJson(event)
        );
        return future.whenComplete((result, ex) -> {
            long ackElapsed = System.currentTimeMillis() - startMs;
            log.info(
                    "[{}] Kafka ACK={}ms partition={} offset={}",
                    resultList.domainGroup().groupId(),
                    ackElapsed,
                    result.getRecordMetadata().partition(),
                    result.getRecordMetadata().offset()
            );
            SendGroupEventResult eventResult = SendGroupEventResult.of();
            dispatcher.publish(SendGroupEventType.SEND_REQUESTED, eventResult, resultList);
        });
    }

    private void printKafkaMetrics() {
        DefaultKafkaProducerFactory<String, Object> factory =
                (DefaultKafkaProducerFactory<String, Object>) template.getProducerFactory();

        Producer<String, Object> producer = factory.createProducer();

        try {
            producer.metrics().forEach((name, metric) -> {
                String metricName = name.name();

                if (metricName.equals("request-latency-avg")
                        || metricName.equals("request-latency-max")
                        || metricName.equals("record-queue-time-avg")
                        || metricName.equals("record-queue-time-max")
                        || metricName.equals("bufferpool-wait-time-total")
                        || metricName.equals("bufferpool-wait-ratio")
                        || metricName.equals("waiting-threads")
                        || metricName.equals("requests-in-flight")) {

                    log.info(
                            "Kafka metric {} = {}",
                            metricName,
                            metric.metricValue()
                    );
                }
            });
        } finally {
            producer.close();
        }
    }
}
