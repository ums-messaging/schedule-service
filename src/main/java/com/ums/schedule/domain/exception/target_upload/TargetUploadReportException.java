package com.ums.schedule.domain.exception.target_upload;

import com.ums.schedule.domain.exception.DomainException;

public abstract class TargetUploadReportException extends DomainException {
    protected TargetUploadReportException(String message) {
        super(message);
    }

    protected TargetUploadReportException(String message, Throwable e) {
        super(message, e);
    }

    protected TargetUploadReportException(Throwable e) {
        super(e);
    }
}
