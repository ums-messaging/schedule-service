package com.ums.schedule.domain.send.email.job;

import com.github.f4b6a3.uuid.UuidCreator;
import com.ums.schedule.domain.request.SendRequest;
import java.util.UUID;

public record SendJob(
        String jobId,
        UUID uploadId,
        String senderKey,
        Long totalCount,
        Long requestId,
        Long completedCount,
        Long failedCount,
        Long sendingCount,
        Long retryingCount
) {
    public static SendJob of(SendRequest sendRequest, UUID uploadId, Long totalCount) {
        return new SendJob(
                UuidCreator.getTimeOrdered().toString(),
                uploadId,
                sendRequest.getSenderKey(),
                totalCount,
                sendRequest.getId(),
                0L,
                0L,
                0L,
                0L
        );
    }
}
