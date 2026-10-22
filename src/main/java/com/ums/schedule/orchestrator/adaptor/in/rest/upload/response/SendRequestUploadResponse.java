package com.ums.schedule.orchestrator.adaptor.in.rest.upload.response;

import com.ums.schedule.orchestrator.application.upload.result.SendRequestUploadResult;
import java.time.LocalDateTime;

public record SendRequestUploadResponse(
        Long id,
        String uploadType,
        String objectKey,
        String status,
        LocalDateTime expiredAt,
        String uploadUrl
) {
    public static SendRequestUploadResponse of(SendRequestUploadResult result) {
        return new SendRequestUploadResponse(
                result.id(),
                result.type(),
                result.key(),
                result.status(),
                result.expiredAt(),
                result.url()
        );
    }
}
