package com.ums.schedule.email.domain.requests.id;

import java.util.UUID;

public record RequestId(
        UUID jobId,
        Long requestId
) {
    public static RequestId of(UUID jobId, Long requestId) {
        return new RequestId(
                jobId, requestId
        );
    }
}
