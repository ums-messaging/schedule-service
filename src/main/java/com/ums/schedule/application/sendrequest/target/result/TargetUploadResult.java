package com.ums.schedule.application.sendrequest.target.result;

import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadReport;

public record TargetUploadResult(
        String reportId,
        String uploadType,
        FileTargetUploadResult fileUploadResult
        ) {
    public static TargetUploadResult of(TargetUploadReport report) {
        return TargetUploadResult.of(report, null);
    }
    public static TargetUploadResult of(TargetUploadReport report, FileTargetUploadResult result) {
        return new TargetUploadResult(
                report.getId().toString(),
                report.getUploadType().code(),
                result
        );
    }
}
