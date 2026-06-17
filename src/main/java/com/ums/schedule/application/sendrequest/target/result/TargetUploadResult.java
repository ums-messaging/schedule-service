package com.ums.schedule.application.sendrequest.target.result;

import com.ums.schedule.adapter.storage.PresigendUrlResponse;
import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadReport;
import com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadTypeEnum;

public record TargetUploadResult(
        Long uploadId,
        TargetUploadTypeEnum uploadType,
        FileTargetUploadResult fileUploadResult,
        JsonTargetUploadResult jsonTargetUploadResult
        ) {
    public static TargetUploadResult of(TargetUploadReport report) {
        return TargetUploadResult.of(report, null);
    }
    public static TargetUploadResult of(TargetUploadReport report, PresigendUrlResponse response) {
        FileTargetUploadResult fileUploadResult = FileTargetUploadResult.of(response);
        return new TargetUploadResult(
                report.getUploadId(),
                report.getUploadType(),
                fileUploadResult,
                JsonTargetUploadResult.of(
                        report.getTotalCount(),
                        report.getSuccessCount(),
                        report.getFailCount()
                )
        );
    }
}
