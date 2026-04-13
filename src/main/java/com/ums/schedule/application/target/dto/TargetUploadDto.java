package com.ums.schedule.application.target.dto;

import com.ums.schedule.adapter.storage.PresigendUrlResponse;
import com.ums.schedule.code.EnumMapperValue;
import com.ums.schedule.domain.target.upload.TargetUpload;
import com.ums.schedule.domain.target.SendReport;

import java.time.LocalDateTime;

public record TargetUploadDto(
        String uploadId,
        EnumMapperValue uploadType,
        String objectKey,
        String presignedUrl,
        Long totalCount,
        Long successCount,
        Long failCount,
        LocalDateTime expiredAt
) {
    public static TargetUploadDto ofResponse(String uploadId, PresigendUrlResponse response) {
        return new TargetUploadDto(
                            uploadId,
                            EnumMapperValue.fromEnumMapperType(TargetUploadTypeEnum.FILE),
                            response.objectKey(),
                            response.presignedUrl(),
                            0L,
                            0L,
                            0L,
                            response.expiredAt());
    }

    public static TargetUploadDto of(TargetUpload targetUpload) {
        SendReport report = targetUpload.getReport();
        return new TargetUploadDto(targetUpload.getUploadId(),
                EnumMapperValue.fromEnumMapperType(TargetUploadTypeEnum.JSON),
                targetUpload.getObjectKey(),
                null,
                report.getTotalCount(),
                report.getSuccessCount(),
                report.getFailCount(),
                null
                );
    }
}
