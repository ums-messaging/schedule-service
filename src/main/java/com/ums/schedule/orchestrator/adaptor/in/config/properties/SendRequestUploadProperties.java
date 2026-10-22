package com.ums.schedule.orchestrator.adaptor.in.config.properties;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@RefreshScope
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ConfigurationProperties(prefix = "send.request.upload")
public class SendRequestUploadProperties {
    private String bucket;
    private String dir;
    private Long fileLimitSize;
}

