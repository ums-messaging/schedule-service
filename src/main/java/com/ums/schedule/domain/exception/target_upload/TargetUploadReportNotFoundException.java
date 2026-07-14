package com.ums.schedule.domain.exception.target_upload;

import com.ums.schedule.common.exception.ResourceNotFoundException;

public class TargetUploadReportNotFoundException extends ResourceNotFoundException {
    protected TargetUploadReportNotFoundException() {
        super("TargetUploadReport");
    }

    public static TargetUploadReportNotFoundException of() {
        return new TargetUploadReportNotFoundException();
    }
}
