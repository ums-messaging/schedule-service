package com.ums.schedule.orchestrator.adaptor.in.config.mapper;

import com.ums.schedule.orchestrator.adaptor.in.config.properties.SendRequestProperties;
import com.ums.schedule.orchestrator.application.requests.config.SendRequestConfig;
import org.springframework.stereotype.Component;

@Component
public class SendRequestConfigMapper {
    public SendRequestConfig toConfig(SendRequestProperties properties) {
        return SendRequestConfig.of(properties);
    }
}
