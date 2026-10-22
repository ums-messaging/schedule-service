package com.ums.schedule.orchestrator.adaptor.in.config.mapper;

import com.ums.schedule.common.code.common.ChannelType;
import com.ums.schedule.orchestrator.adaptor.in.config.properties.SendRequestChannelProperties;
import org.springframework.stereotype.Component;

@Component
public class SendRequestChannelConfigMapper {
    public String findTopicByChannel(ChannelType channelType, SendRequestChannelProperties properties) {
        switch (channelType) {
            case EMAIL -> {
                return properties.getEmail().getTopic();
            }
        }
        return null;
    }
}
