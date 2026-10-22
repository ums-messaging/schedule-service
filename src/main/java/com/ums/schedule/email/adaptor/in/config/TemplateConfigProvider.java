package com.ums.schedule.email.adaptor.in.config;

import com.ums.schedule.email.adaptor.in.config.mapper.SendJobConfigMapper;
import com.ums.schedule.email.adaptor.in.config.mapper.TemplateConfigMapper;
import com.ums.schedule.email.adaptor.in.config.properties.SendJobProperties;
import com.ums.schedule.email.adaptor.in.config.properties.TemplateProperties;
import com.ums.schedule.email.application.send.job.config.SendJobConfig;
import com.ums.schedule.email.application.template.config.TemplateConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TemplateConfigProvider {
    private final TemplateConfigMapper mapper;
    private final TemplateProperties properties;

    public TemplateConfig load() {
        return mapper.toConfig(properties);
    }
}
