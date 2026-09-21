package com.ums.schedule.application.ums.common.request.model;

import com.github.f4b6a3.uuid.UuidCreator;
import com.ums.schedule.common.code.common.ChannelType;
import com.ums.schedule.domain.request.SendRequest;

import java.util.UUID;

public record SendRequestRequestedEvent(
        String jobId,
        Long requestId,
        UUID uploadId,
        ChannelType channelType
) {
    public static SendRequestRequestedEvent of(UUID uploadId, SendRequest sendRequest) {
        return new SendRequestRequestedEvent(
                UuidCreator.getTimeOrdered().toString(),
                sendRequest.getId(),
                uploadId,
                sendRequest.getChannelType()
        );
    }
}
