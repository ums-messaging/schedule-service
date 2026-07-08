package com.ums.schedule.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@RefreshScope
@ConfigurationProperties(prefix = "template.email")
public record EmailTemplateProperties(
        String templateKeyPrefix,
        String imageKeySuffix,
        String attachmentKeySuffix
) {
}
