package com.ums.schedule.config.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RefreshScope
@ConfigurationProperties(prefix = "template.email")
public class EmailTemplateProperties {
    private String templateKeyPrefix;
    private String imageKeySuffix;
    private String attachmentKeySuffix;
}
