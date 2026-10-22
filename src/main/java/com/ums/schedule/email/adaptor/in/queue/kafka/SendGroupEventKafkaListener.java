package com.ums.schedule.email.adaptor.in.queue.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendGroupEventKafkaListener {

    @KafkaListener(
            topics = "email-send-target-group-event",
            groupId = "email-send-target-group-success"
    )
    public void listenSuccess(ConsumerRecord<String, String> record) {
        String payload = record.value();
    }

    @KafkaListener(
            topics = "email-send-target-group-event",
            groupId = "email-send-target-group-failure"
    )
    public void listenFailure(ConsumerRecord<String, String> record) {
        String payload = record.value();
    }
}
