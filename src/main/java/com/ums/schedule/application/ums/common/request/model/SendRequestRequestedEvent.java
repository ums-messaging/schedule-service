package com.ums.schedule.application.ums.common.request.model;

import com.ums.schedule.domain.request.SendRequest;

public record SendRequestRequestedEvent(
        Long requestId
) {
    public static SendRequestRequestedEvent of(SendRequest sendRequest) {
        return new SendRequestRequestedEvent(
                sendRequest.getId()
        );
    }
}
