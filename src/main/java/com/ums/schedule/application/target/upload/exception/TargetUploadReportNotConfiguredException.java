package com.ums.schedule.application.target.upload.exception;

import com.ums.schedule.common.code.target_upload.TargetUploadUploadPrefix;
import com.ums.schedule.common.exception.NotConfiguredException;

public class TargetUploadReportNotConfiguredException extends NotConfiguredException {
    protected TargetUploadReportNotConfiguredException(String confValue) {
        super(confValue);
    }

    public static TargetUploadReportNotConfiguredException of(TargetUploadUploadPrefix prefix) {
        return new TargetUploadReportNotConfiguredException(prefix.description());
    }
}
