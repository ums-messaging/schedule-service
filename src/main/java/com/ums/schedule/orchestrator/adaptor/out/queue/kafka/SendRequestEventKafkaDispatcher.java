package com.ums.schedule.orchestrator.adaptor.out.queue.kafka;

import com.ums.schedule.common.code.request.SendRequestEvent;
import com.ums.schedule.orchestrator.adaptor.out.queue.SendRequestEventDispatcher;
import com.ums.schedule.common.util.JsonUtil;
import com.ums.schedule.orchestrator.application.channel.event.ChannelRequestedEvent;
import com.ums.schedule.orchestrator.application.requests.SendRequestedEvent;
import com.ums.schedule.orchestrator.application.requests.result.SendRequestRequestContext;
import com.ums.schedule.orchestrator.domain.event.SendRequest;
import com.ums.schedule.orchestrator.domain.event.SendRequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SendRequestEventKafkaDispatcher implements SendRequestEventDispatcher {
    private final SendRequestRepository repository;
    private final KafkaTemplate<String, Object> template;

    public SendRequestEventKafkaDispatcher(@Qualifier("kafkaTemplate")
                                                   KafkaTemplate<String, Object> template,
                                           SendRequestRepository repository) {
        this.template = template;
        this.repository = repository;
    }

    @Override
    public boolean supports(String mode) {
        return "kafka".equals(mode);
    }

    public void publish(String topic, SendRequestRequestContext context) {
        SendRequest sendRequest = context.request();
        sendRequest.requestSend(context.schedule());
        repository.updateStatus(sendRequest);
        SendRequestedEvent event = SendRequestedEvent.of(context);
        send(topic, event);
    }

    private void send(String topic, SendRequestedEvent event) {
        template.send(topic, String.valueOf(event.requestId()), JsonUtil.toJson(event))
                .whenComplete((res, ex) -> {
                    if (ex != null) {
                        log.error("❌ Kafka publish failed", ex);
                    } else {
                        log.info(
                                "✅ Kafka published topic={}, partition={}, offset={}",
                                res.getRecordMetadata().topic(),
                                res.getRecordMetadata().partition(),
                                res.getRecordMetadata().offset()
                        );
                    }
                });
    }
}
