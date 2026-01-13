package com.ums.schedule.intergration.kafka;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class KafkaIntegrationTest {
    private static final String TEST_SCHEDULE_TOPIC = "test-schedule-topic";

    @Autowired KafkaTestProducer producer;
    @Autowired KafkaTestConsumer consumer;

    @Test
    public void should_send_and_consume_message() throws InterruptedException {
        // given
        String payload = "hello";

        // when
        producer.send(TEST_SCHEDULE_TOPIC, payload);

        // then
        String result = consumer.awaitMessage(500);
        assertThat(result).isEqualTo(payload);
    }
}
