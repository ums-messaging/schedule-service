package com.ums.schedule.email.adaptor.in.config.properties;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@RefreshScope
@ConfigurationProperties(prefix = "email.template")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TemplateProperties {
    private String uploadDir;
    private String imageDir;
    private String attachmentsDir;
}
