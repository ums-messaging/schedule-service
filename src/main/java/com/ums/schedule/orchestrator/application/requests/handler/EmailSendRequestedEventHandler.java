package com.ums.schedule.orchestrator.application.requests.handler;

import com.ums.schedule.common.code.common.ChannelType;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.orchestrator.adaptor.in.config.SendRequestChannelConfigProvider;
import com.ums.schedule.orchestrator.adaptor.out.queue.kafka.SendRequestEventKafkaDispatcher;
import com.ums.schedule.orchestrator.application.requests.result.SendRequestRequestContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailSendRequestedEventHandler implements SendRendRequestedHandler {
    private final SendRequestChannelConfigProvider provider;
    private final SendRequestEventKafkaDispatcher dispatcher;

    @Override
    public boolean supports(EnumMapperValue channelValue) {
        return ChannelType.EMAIL == ChannelType.valueOf(channelValue.code());
    }

    @Override
    public void handle(SendRequestRequestContext context) {
        String topic = provider.getTopic(context.request().getChannelType());
        dispatcher.publish(topic, context);
    }
}
