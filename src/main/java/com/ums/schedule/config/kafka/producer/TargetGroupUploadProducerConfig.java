package com.ums.schedule.config.kafka.producer;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewPartitions;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Configuration
public class TargetGroupUploadProducerConfig {
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();

        // 1. 기본 브로커 정보 및 직렬화 설정
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        // 2. 대용량 페이로드 최적화 압축 설정 (zstd)
        configProps.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "zstd");

        // 3. 배치 튜닝: 대용량 데이터가 효율적으로 압축되도록 공간과 대기시간 확보
        configProps.put(ProducerConfig.BATCH_SIZE_CONFIG, 131072); // 128 KB
        configProps.put(ProducerConfig.LINGER_MS_CONFIG, 20);

        configProps.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, 5242880); // 5 MB

//        try (AdminClient admin = AdminClient.create(
//                Map.of(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092"))) {
//
//            admin.createPartitions(
//                    Map.of(
//                            "email-target-group-upload",
//                            NewPartitions.increaseTo(3)
//                    )
//            ).all().get();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        return new DefaultKafkaProducerFactory<>(configProps);
    }
    @Bean("targetGroupUploadTemplate")
    public KafkaTemplate<String, Object> targetGroupUploadTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
