package com.ums.schedule.orchestrator.adaptor.in.config;

import com.ums.schedule.common.code.common.ChannelType;
import com.ums.schedule.orchestrator.adaptor.in.config.mapper.SendRequestChannelConfigMapper;
import com.ums.schedule.orchestrator.adaptor.in.config.properties.SendRequestChannelProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendRequestChannelConfigProvider {
    private final SendRequestChannelProperties properties;
    private final SendRequestChannelConfigMapper mapper;

    public String getTopic(ChannelType channelType) {
        return mapper.findTopicByChannel(channelType, properties);
    }
}
