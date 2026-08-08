package com.ums.schedule.application.target.processor.model;

import com.ums.schedule.adapter.storage.AwsS3FileMetadataResponse;
import com.ums.schedule.common.code.target_upload.TargetUploadStatus;
import com.ums.schedule.domain.target.upload.TargetUploadReport;

import java.util.UUID;

public record FileTargetUploadRequestResult(
        UUID id,
        TargetUploadStatus status,
        Long fileSize
) {
    public static FileTargetUploadRequestResult of(TargetUploadReport report, AwsS3FileMetadataResponse response) {
        return new FileTargetUploadRequestResult(
                report.getId(),
                report.getState().getCurrentCode(),
                response.contentLength()
        );
    }
}
