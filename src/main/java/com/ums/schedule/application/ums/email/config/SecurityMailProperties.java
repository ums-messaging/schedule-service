package com.ums.schedule.application.ums.email.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;


@RefreshScope
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ConfigurationProperties(prefix = "email.message.security")
public class SecurityMailProperties {
    private String defaultEncryptType;
    private String defaultPasswordHash;
    private String defaultPasswordPolicy;
    private String defaultPermissionMask;
    private String ownerPassword;

}
