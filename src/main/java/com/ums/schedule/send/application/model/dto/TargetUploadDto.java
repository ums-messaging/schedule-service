package com.ums.schedule.send.application.model.dto;

import com.ums.schedule.send.application.model.response.PresigedUrlResponse;

import java.time.LocalDateTime;

public record TargetUploadDto(
        String objectKey,
        String presignedUrl,
        Long fileSize,
        LocalDateTime expiredAt
) {

    public static TargetUploadDto of(Long totalCount) {
        return new TargetUploadDto(null, null, null, null);
    }

    public static TargetUploadDto ofResponse(PresigedUrlResponse response) {
        return new TargetUploadDto(response.objectKey(),
                                    response.presignedUrl(),
                                    response.fileSize(),
                                    response.expiredAt());
    }
}
