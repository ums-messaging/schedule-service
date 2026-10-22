package com.ums.schedule.email.adaptor.in.config.mapper;

import com.ums.schedule.email.adaptor.in.config.properties.TemplateProperties;
import com.ums.schedule.email.application.template.config.TemplateConfig;
import org.springframework.stereotype.Component;

@Component
public class TemplateConfigMapper {

    public TemplateConfig toConfig(TemplateProperties properties) {
        return TemplateConfig.toConfig(properties);
    }
}
