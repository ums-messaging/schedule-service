package com.ums.schedule.application.target.upload.model;

import com.ums.schedule.application.target.upload.handler.FileTargetUploadResult;
import com.ums.schedule.application.ums.common.request.model.TargetUploadCreateSummary;
import com.ums.schedule.common.code.target_upload.TargetUploadStatus;
import com.ums.schedule.common.code.target_upload.TargetUploadType;
import com.ums.schedule.domain.target.upload.TargetUploadReport;

public record TargetUploadResult(
        String reportId,
        TargetUploadType uploadType,
        TargetUploadStatus status,
        FileTargetUploadResult fileUploadResult
        ) {
    public static TargetUploadResult of(TargetUploadReport report) {
        return TargetUploadResult.of(report, null);
    }
    public static TargetUploadResult of(TargetUploadReport report, FileTargetUploadResult result) {
        return new TargetUploadResult(
                report.getId().toString(),
                report.getUploadType(),
                report.getState().getCurrentCode(),
                result
        );
    }

    public TargetUploadCreateSummary toSummary() {
        return TargetUploadCreateSummary.of(this);
    }
}
