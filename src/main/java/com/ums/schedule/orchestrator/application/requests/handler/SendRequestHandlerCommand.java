package com.ums.schedule.orchestrator.application.requests.handler;

import com.ums.schedule.orchestrator.domain.common.ProvideUrl;
import com.ums.schedule.orchestrator.domain.event.SendRequest;

import java.time.LocalDateTime;

public record SendRequestHandlerCommand(
        String requestId,
        String downloadUrl,
        LocalDateTime expiredAt
) {
}
