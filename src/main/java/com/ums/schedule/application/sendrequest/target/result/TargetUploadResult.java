package com.ums.schedule.application.sendrequest.target.result;

import com.ums.schedule.adapter.storage.PresigendUrlResponse;
import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadReport;
import com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadTypeEnum;

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
                String.valueOf(report.getUploadId()),
                report.getUploadType().code(),
                result
        );
    }
}
