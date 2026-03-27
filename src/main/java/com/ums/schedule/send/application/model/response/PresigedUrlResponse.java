package com.ums.schedule.send.application.model.response;

import java.time.LocalDateTime;

public record PresigedUrlResponse(
    String objectKey,
    String presignedUrl,
    Long fileSize,
    LocalDateTime expiredAt
) {
}
