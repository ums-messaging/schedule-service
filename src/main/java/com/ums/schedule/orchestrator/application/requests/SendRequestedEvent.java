package com.ums.schedule.orchestrator.application.requests;

import com.ums.schedule.common.code.common.ChannelType;
import com.ums.schedule.orchestrator.application.requests.result.SendRequestRequestContext;
import com.ums.schedule.orchestrator.domain.common.ProvideUrl;
import com.ums.schedule.orchestrator.domain.event.SendRequest;
import com.ums.schedule.orchestrator.domain.schedule.Schedule;

import java.time.format.DateTimeFormatter;

public record SendRequestedEvent(
        ChannelType channelType,
        Long requestId,
        String downloadUrl,
        String expiredAt
) {
    public static SendRequestedEvent of(SendRequestRequestContext context) {
        SendRequest request = context.request();
        ProvideUrl provideUrl = context.provideUrl();

        return new SendRequestedEvent(
                request.getChannelType(),
                request.getId(),
                provideUrl.provideUrl(),
                provideUrl.expiredAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
    }

}
