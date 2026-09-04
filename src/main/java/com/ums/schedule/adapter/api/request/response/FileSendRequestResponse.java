package com.ums.schedule.adapter.api.request.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.ums.schedule.application.ums.common.request.model.TargetUploadCreateSummary;

import java.time.Instant;

public record FileSendRequestResponse(
        @JsonUnwrapped
        TargetUploadResponse currentTargetUpload,
        String objectKey,
        String presignedUrl,
        Instant expiredAt
) {
    public static FileSendRequestResponse of(TargetUploadCreateSummary summary) {
        return new FileSendRequestResponse(
                TargetUploadResponse.of(summary),
                summary.objectKey(),
                summary.presignedUrl(),
                summary.expiredAt()
        );
    }
}
