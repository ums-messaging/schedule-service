package com.ums.schedule.orchestrator.application.upload.result;

import com.ums.schedule.orchestrator.domain.upload.SendRequestUpload;

import java.time.LocalDateTime;

public record SendRequestUploadResult(
        Long id,
        String type,
        String status,
        String key,
        String url,
        LocalDateTime createdAt,
        LocalDateTime expiredAt
        ) {

    public static SendRequestUploadResult of(SendRequestUpload upload) {
        return new SendRequestUploadResult(
                upload.getId(),
                upload.getType().code(),
                upload.getState().getCurrentCode().code(),
                upload.objectKey(),
                upload.provideUrl(),
                upload.getCreatedAt(),
                upload.expiredAt()
        );
    }
}
