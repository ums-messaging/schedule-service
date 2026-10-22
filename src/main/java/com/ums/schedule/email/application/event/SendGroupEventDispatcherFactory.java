package com.ums.schedule.email.application.event;

import com.ums.schedule.common.code.email.EmailResultCode;
import com.ums.schedule.email.adaptor.out.queue.kafka.SendGroupEventKafkaDispatcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SendGroupEventDispatcherFactory {
    private final List<SendGroupEventKafkaFactory> factoryList;

    public SendGroupEventKafkaFactory findGroupEventDispatcher(EmailResultCode resultCode) {
        return factoryList.stream()
                .filter(f -> f.supports(resultCode))
                .findFirst()
                .orElseThrow();
    }
}
