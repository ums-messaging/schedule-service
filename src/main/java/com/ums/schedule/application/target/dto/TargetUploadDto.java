package com.ums.schedule.application.target.dto;

import com.ums.schedule.adapter.storage.PresigendUrlResponse;
import com.ums.schedule.code.EnumMapperValue;
import com.ums.schedule.code.send.TargetUploadTypeEnum;
import com.ums.schedule.domain.target.upload.TargetUpload;

import java.time.LocalDateTime;

public record TargetUploadDto(
        Long uploadId,
        EnumMapperValue uploadType,
        String objectKey,
        String presignedUrl,
        LocalDateTime expiredAt
) {
    public static TargetUploadDto ofResponse(Long uploadId, PresigendUrlResponse response) {
        return new TargetUploadDto(
                            uploadId,
                            EnumMapperValue.fromEnumMapperType(TargetUploadTypeEnum.FILE),
                            response.objectKey(),
                            response.presignedUrl(),
                            response.expiredAt());
    }

    public static TargetUploadDto of(TargetUpload targetUpload) {
        return new TargetUploadDto(targetUpload.getUploadId(),
                EnumMapperValue.fromEnumMapperType(TargetUploadTypeEnum.JSON),
                targetUpload.getObjectKey(),
                null,
                null
                );
    }
}
