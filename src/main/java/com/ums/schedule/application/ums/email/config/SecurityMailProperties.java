package com.ums.schedule.application.ums.email.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;


@RefreshScope
@ConfigurationProperties(prefix = "email.message.security")
public record SecurityMailProperties(
        String defaultEncryptType,
        String defaultPasswordHash,
        String defaultPasswordPolicy,
        String defaultPermissionMask
) {

}
