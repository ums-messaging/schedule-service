package com.ums.schedule.application.ums.common.request.model;

import com.ums.schedule.application.target.upload.handler.FileTargetUploadResult;
import com.ums.schedule.application.target.upload.model.TargetUploadResult;
import com.ums.schedule.common.code.target_upload.TargetUploadStatus;
import com.ums.schedule.common.code.target_upload.TargetUploadType;

import java.time.Instant;

public record TargetUploadCreateSummary(
        String uploadId,
        TargetUploadType uploadType,
        TargetUploadStatus status,
        String objectKey,
        String presignedUrl,
        Instant expiredAt
) {
    public static TargetUploadCreateSummary of(TargetUploadResult result) {
        TargetUploadType uploadType = result.uploadType();
        if(uploadType == TargetUploadType.FILE) {
            return TargetUploadCreateSummary.fileOf(result, result.fileUploadResult());
        }
        return new TargetUploadCreateSummary(
                result.reportId(),
                TargetUploadType.JSON,
                result.status(),
                null,
                null,
                null
        );
    }

    private static TargetUploadCreateSummary fileOf(TargetUploadResult result, FileTargetUploadResult fileResult) {
        return new TargetUploadCreateSummary(
                result.reportId(),
                TargetUploadType.JSON,
                result.status(),
                fileResult.objectKey(),
                fileResult.uploadUrl(),
                fileResult.expiredAt()
        );
    }
}
