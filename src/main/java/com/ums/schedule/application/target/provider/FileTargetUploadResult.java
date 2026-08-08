package com.ums.schedule.application.target.provider;

import com.ums.schedule.adapter.storage.PresigendUrlResponse;
import com.ums.schedule.domain.target.upload.TargetUploadReport;

import java.time.Instant;

public record FileTargetUploadResult(
        String objectKey,
        String uploadUrl,
        Instant expiredAt
) {
    public static FileTargetUploadResult of(PresigendUrlResponse response) {
        return new FileTargetUploadResult(
                response.objectKey(),
                response.presignedUrl(),
                response.expiredAt()
        );
    }
}
