package com.ums.schedule.config.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@RefreshScope
@ConfigurationProperties(prefix = "target.upload.prefix")
@Getter
@AllArgsConstructor
@ToString
public class TargetUploadProperties {
    private String bucket;
    private String uploadKey;
    private String downloadKey;
    private Integer uploadMaxSize;
}
