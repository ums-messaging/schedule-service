package com.ums.schedule.adapter.queue;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EmailSendConsumer {
    @KafkaListener(topics = "Email")
    public void consume(String message) {
        try {
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
