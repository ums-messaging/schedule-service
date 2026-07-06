package com.ums.schedule.application.sendrequest.target.result;

import com.ums.schedule.adapter.storage.PresigendUrlResponse;

import java.time.LocalDateTime;

public record FileTargetUploadResult(
        String objectKey,
        String uploadUrl,
        LocalDateTime expiredAt
) {
    protected static FileTargetUploadResult of(PresigendUrlResponse response) {
        return new FileTargetUploadResult(
                response.objectKey(),
                response.presignedUrl(),
                response.expiredAt()
        );
    }
}
