package com.ums.schedule.email.adaptor.in.config;

import com.ums.schedule.email.adaptor.in.config.mapper.MessageConfigMapper;
import com.ums.schedule.email.adaptor.in.config.properties.MessageProperties;
import com.ums.schedule.email.application.message.config.SecurityMailConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageConfigProvider {
    private final MessageConfigMapper mapper;
    private final MessageProperties properties;

    public SecurityMailConfig loadSecurityMail() {
        return mapper.toSecurityConfig(properties);
    }
}
