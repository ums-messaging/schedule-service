package com.ums.schedule.orchestrator.application.requests.result;

import com.ums.schedule.orchestrator.adaptor.out.storage.PresigendUrlResponse;
import com.ums.schedule.orchestrator.domain.common.ProvideUrl;
import com.ums.schedule.orchestrator.domain.event.SendRequest;
import com.ums.schedule.orchestrator.domain.schedule.Schedule;


public record SendRequestRequestContext(
        Schedule schedule,
        SendRequest request,
        ProvideUrl provideUrl
) {
    public static SendRequestRequestContext of(SendRequest sendRequest, Schedule schedule, PresigendUrlResponse response) {
        ProvideUrl provideUrl = ProvideUrl.of(response.objectKey(), response.presignedUrl(), response.expiredAt());
        return new SendRequestRequestContext(
                schedule,
                sendRequest,
                provideUrl
        );
    }
}
