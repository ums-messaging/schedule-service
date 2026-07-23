package com.ums.schedule.adapter.api.request.response;

import com.ums.schedule.application.ums.common.request.model.TargetUploadCreateSummary;

public record TargetUploadResponse(
        String uploadId,
        String uploadType,
        String status
) {
    public static TargetUploadResponse of(TargetUploadCreateSummary summary) {
        return new TargetUploadResponse(
                summary.uploadId(),
                summary.uploadType().code(),
                summary.status().code()
        );
    }
}
