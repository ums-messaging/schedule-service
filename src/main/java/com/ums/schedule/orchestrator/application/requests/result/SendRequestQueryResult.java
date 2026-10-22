package com.ums.schedule.orchestrator.application.requests.result;

import com.ums.schedule.orchestrator.domain.event.SendRequest;
import com.ums.schedule.orchestrator.domain.schedule.Schedule;
import com.ums.schedule.orchestrator.domain.upload.SendRequestUpload;

public record SendRequestQueryResult(
        Schedule schedule,
        SendRequest sendRequest,
        SendRequestUpload sendRequestUpload
) {

    public static SendRequestQueryResult of(Schedule schedule, SendRequest sendRequest, SendRequestUpload sendRequestUpload) {
        return new SendRequestQueryResult(
                schedule, sendRequest, sendRequestUpload
        );
    }
}
