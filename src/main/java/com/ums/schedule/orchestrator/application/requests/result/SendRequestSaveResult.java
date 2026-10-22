package com.ums.schedule.orchestrator.application.requests.result;

import com.ums.schedule.orchestrator.domain.event.SendRequest;
import com.ums.schedule.orchestrator.domain.schedule.Schedule;

public record SendRequestSaveResult(
        Schedule schedule,
        SendRequest sendRequest
) {
    public static SendRequestSaveResult of(Schedule schedule, SendRequest sendRequest) {
        return new SendRequestSaveResult(
                schedule, sendRequest
        );
    }
}
