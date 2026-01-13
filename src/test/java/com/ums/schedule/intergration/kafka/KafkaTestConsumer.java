package com.ums.schedule.intergration.kafka;

import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Component
@Profile("test")
public class KafkaTestConsumer {
    private static final String TEST_SCHEDULE_TOPIC = "test-schedule-topic";
    private static final String TEST_SCHEDULE_GROUP = "test-schedule-group";

    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();
    @KafkaListener(topics = TEST_SCHEDULE_TOPIC, groupId = TEST_SCHEDULE_GROUP)
    public void consume(String message) {
        try {
            queue.offer(message);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String awaitMessage(long timeoutMs) throws InterruptedException {
            return queue.poll(timeoutMs, TimeUnit.SECONDS);
    }
}
