package com.ums.schedule.email.adaptor.in.config.properties;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@RefreshScope
@ConfigurationProperties(prefix = "email.job")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SendJobProperties {
    private Integer batchSize;
    private Integer partitionSize;
}
