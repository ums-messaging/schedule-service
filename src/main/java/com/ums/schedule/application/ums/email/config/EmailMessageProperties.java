package com.ums.schedule.application.ums.email.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@Getter
@RefreshScope
@ConfigurationProperties(prefix = "send.message.email")
public class EmailMessageProperties {
    private String convertFileKeyTemplate;
}
