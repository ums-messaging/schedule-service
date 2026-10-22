package com.ums.schedule.orchestrator.application.requests.result;

import com.ums.schedule.orchestrator.domain.event.SendRequest;
import com.ums.schedule.orchestrator.domain.schedule.Schedule;

public record SendRequestUpdateStateResult(
        SendRequest sendRequest,
        Schedule schedule,
        String objectKey
) {
    public static SendRequestUpdateStateResult of(SendRequest sendRequest, Schedule schedule, String objectKey) {
        return new SendRequestUpdateStateResult(sendRequest, schedule, objectKey);
    }
}
