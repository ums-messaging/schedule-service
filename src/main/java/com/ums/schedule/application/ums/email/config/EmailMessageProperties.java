package com.ums.schedule.application.ums.email.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@Getter
@Setter
@RefreshScope
@ConfigurationProperties(prefix = "send.message.email")
public class EmailMessageProperties {
    private String convertFileKeyTemplate;
}
