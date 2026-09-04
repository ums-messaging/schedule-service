package com.ums.schedule.application.ums.common.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@Getter
@RefreshScope
@ConfigurationProperties(prefix = "send.message")
public class SendMessageProperties {
    private String advertisingPrefix;
}
