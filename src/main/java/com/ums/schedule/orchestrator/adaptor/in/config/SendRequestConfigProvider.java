package com.ums.schedule.orchestrator.adaptor.in.config;

import com.ums.schedule.orchestrator.adaptor.in.config.mapper.SendRequestConfigMapper;
import com.ums.schedule.orchestrator.adaptor.in.config.properties.SendRequestProperties;
import com.ums.schedule.orchestrator.application.requests.config.SendRequestConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendRequestConfigProvider {
    private final SendRequestConfigMapper mapper;
    private final SendRequestProperties properties;

    public SendRequestConfig loadConfig() {
        return mapper.toConfig(properties);
    }
}
