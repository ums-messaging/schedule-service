package com.ums.schedule.orchestrator.application.requests.model;

import com.ums.schedule.common.code.request.SendRequestEvent;

public record SendRequestRequestResult(
        String requestId,
        SendRequestEvent event
) {
}
