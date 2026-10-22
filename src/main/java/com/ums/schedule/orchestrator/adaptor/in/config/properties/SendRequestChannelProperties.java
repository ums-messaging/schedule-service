package com.ums.schedule.orchestrator.adaptor.in.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@RefreshScope
@Getter
@ConfigurationProperties(prefix = "send.request.channel")
public class SendRequestChannelProperties {
    private SendRequestEmailProperties email = new SendRequestEmailProperties();

    @Getter @Setter
    public static class SendRequestEmailProperties {
        private String topic;
    }

}
