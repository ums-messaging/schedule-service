package com.ums.schedule.common.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@RefreshScope
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "send.request")
public class SendRequestProperties {
    private Integer retryCount;

}

