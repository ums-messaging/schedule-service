package com.ums.schedule.email.application.send.job.command;

import com.github.f4b6a3.uuid.UuidCreator;
import com.ums.schedule.email.application.requests.result.SendRequestQueryResult;

import java.util.UUID;

public record JobCommand(
        UUID jobId,
        Long requestId,
        SendRequestQueryResult result,
        String downloadUrl
) {
    public static JobCommand of(SendRequestQueryResult result, Long requestId, String downloadUrl) {
        return new JobCommand(
                UuidCreator.getTimeOrdered(),
                requestId,
                result,
                downloadUrl
        );
    }
}
