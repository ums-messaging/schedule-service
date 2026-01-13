package com.ums.schedule.intergration.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class KafkaTestProducer {
    @Autowired
    private KafkaTemplate<String, byte[]> kafkaTemplate;

    public void send(String topic, String message) {
        kafkaTemplate.send(topic, message.getBytes(StandardCharsets.UTF_8));
    }

}
