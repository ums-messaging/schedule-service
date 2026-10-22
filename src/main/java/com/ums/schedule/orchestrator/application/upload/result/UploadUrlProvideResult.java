package com.ums.schedule.orchestrator.application.upload.result;

import com.ums.schedule.orchestrator.adaptor.out.storage.PresigendUrlResponse;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public record UploadUrlProvideResult(
        String objectKey,
        String uploadUrl,
        LocalDateTime expiredAt
) {
    public static UploadUrlProvideResult of(PresigendUrlResponse response) {
        Instant instant = response.expiredAt();
        return new UploadUrlProvideResult(
                response.objectKey(),
                response.presignedUrl(),
                LocalDateTime.ofInstant(instant, ZoneId.of("Asia/Seoul"))
        );
    }

    public static UploadUrlProvideResult of(String objectKey, String url) {
        return new UploadUrlProvideResult(
                objectKey,
                url,
                null
        );
    }
}
