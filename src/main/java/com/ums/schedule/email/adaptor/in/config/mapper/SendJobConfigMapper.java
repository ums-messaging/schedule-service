package com.ums.schedule.email.adaptor.in.config.mapper;

import com.ums.schedule.email.adaptor.in.config.properties.SendJobProperties;
import com.ums.schedule.email.application.send.job.config.SendJobConfig;
import org.springframework.stereotype.Component;

@Component
public class SendJobConfigMapper {
    public SendJobConfig toConfig(SendJobProperties properties) {
        return SendJobConfig.from(properties);
    }
}
