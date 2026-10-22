package com.ums.schedule.orchestrator.adaptor.in.rest.requests.response;

import com.ums.schedule.orchestrator.application.requests.model.SendRequestCreateResult;

public record SendRequestCreateResponse(
        Long requestId,
        String channelType,
        String customerKey
) {
    public static SendRequestCreateResponse of(SendRequestCreateResult result) {
        return new SendRequestCreateResponse(
                result.requestId(),
                result.channelType().code(),
                result.customerKey()
        );
    }
}
