package com.ums.schedule.email.adaptor.in.config.properties;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@RefreshScope
@ConfigurationProperties(prefix = "email.message")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageProperties {
    private SecurityProperties securityProperties = new SecurityProperties();

    @Getter
    @Setter
    public static class SecurityProperties {
        private String encryptionType;
        private String passwordHash;
        private String permissionMask;
        private String ownerPassword;
    }
}
