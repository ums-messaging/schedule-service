package com.ums.schedule.application.ums.common.send.model;

public record EmailJobEvent(
        String jobId,
        Long requestId,
        String uploadId
) {
}
