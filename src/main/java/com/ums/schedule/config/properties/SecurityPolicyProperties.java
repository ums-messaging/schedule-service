package com.ums.schedule.config.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
public class SecurityPolicyProperties {
    private String ownerPassword;
}
