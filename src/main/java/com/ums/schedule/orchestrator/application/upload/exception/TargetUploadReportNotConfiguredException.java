package com.ums.schedule.orchestrator.application.upload.exception;

import com.ums.schedule.common.code.target_upload.TargetUploadConfiguration;
import com.ums.schedule.common.exception.NotConfiguredException;

public class TargetUploadReportNotConfiguredException extends NotConfiguredException {
    protected TargetUploadReportNotConfiguredException(String confValue) {
        super(confValue);
    }

    public static TargetUploadReportNotConfiguredException of(TargetUploadConfiguration prefix) {
        return new TargetUploadReportNotConfiguredException(prefix.description());
    }
}
