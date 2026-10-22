package com.ums.schedule.email.adaptor.in.config;

import com.ums.schedule.email.adaptor.in.config.mapper.SendJobConfigMapper;
import com.ums.schedule.email.adaptor.in.config.properties.SendJobProperties;
import com.ums.schedule.email.application.send.job.config.SendJobConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendJobConfigProvider {
    private final SendJobProperties properties;
    private final SendJobConfigMapper mapper;

    public SendJobConfig loadSendJobConfig() {
        return mapper.toConfig(properties);
    }
}
