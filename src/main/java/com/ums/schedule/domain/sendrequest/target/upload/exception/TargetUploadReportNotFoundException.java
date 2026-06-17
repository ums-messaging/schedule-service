package com.ums.schedule.domain.sendrequest.target.upload.exception;

import com.ums.schedule.common.exception.ResourceNotFoundException;

public class TargetUploadReportNotFoundException extends ResourceNotFoundException {
    protected TargetUploadReportNotFoundException() {
        super("TargetUploadReport");
    }

    public static TargetUploadReportNotFoundException of() {
        return new TargetUploadReportNotFoundException();
    }
}
